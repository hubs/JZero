package com.jzero.db.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.AsyncQueryRunner;

import com.jzero.core.MInit;
import com.jzero.core.MReport;
import com.jzero.db.cache.MCache;
import com.jzero.db.exp.MHandler;
import com.jzero.log.Log;
import com.jzero.log.LogEnum;
import com.jzero.pool.PoolFactory;
import com.jzero.util.MCheck;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;
import com.jzero.util.MSee;
import com.jzero.util.MSee.IAtom;
import com.jzero.util.MSee.IAtom2;
import com.jzero.util.MSee.IAtom3;
import com.jzero.util.MSee.IAtom4;

/**
 * 2012-10-3: wangujqw@gmail.com
 */
public abstract class CBase implements IDb {
	private static Connection connection = null;

	public Connection getConnection() {
		if (MCheck.isNull(connection)) {
			try {
				connection = PoolFactory.getConnection();
			} catch (Exception e) {
				MInit.get().getMb().getTextRender(e.getMessage()).render();
			}
		}
		return connection;
	}
	public List<MRecord> select(final String sql, final Object... params){
		List<MRecord> data = null;
		if (MCache.me().isEnabled()) {
			data = MCache.me().read(sql);
		}
		if(MCheck.isNull(data)){
			try{
				data=select(getConnection(), sql,params);
			}catch(Exception e){//2013-1-8,如果此处出现异常,可能是mysql断掉了原因,所在需要在这重新连接一下
				connection = PoolFactory.getConnection();
				data=select(connection, sql,params);
			}
		}else {
			MSee.ok_sql(new IAtom() {
				public void run() throws Exception {
					MReport.doFileReport(sql);
				}
			}, is_dev());
		}
		return data;
	}
	public List<MRecord> select(final Connection conn,final String sql, final Object... params) {
			MSee.ok_sql(new IAtom() {
				public void run() throws Exception {
					Log.me().write_log(LogEnum.DEBUG, "〖查询〗SQL:" + sql);
				}
			}, is_show_sql());

			MSee.ok_sql(new IAtom() {
				public void run() throws Exception {
					MReport.doSQLReport(sql, params);
				}
			}, is_dev());

			List<MRecord> data = MSee.ok_time(new IAtom2() {
				public List<MRecord> run() throws Exception {
					AsyncQueryRunner query = new AsyncQueryRunner(Executors
							.newCachedThreadPool());
					return query.query(conn, sql, new MHandler(),
							params).get();
				}
			}, is_run_time());

			if (MCache.me().isEnabled()) {
				MSee.ok_time(new IAtom3() {
					public void run(List<MRecord> inDatas) throws Exception {
						MCache.me().write(sql, inDatas);
					}
				}, is_run_time(), data);
			}
		return data;
	}

	public List<MRecord> select(String table, String where, String field,Object... params) {
		return select(getConnection(),table,where,field,params);
	}
	public List<MRecord> select(Connection conn,String table, String where, String field,
			Object... params) {
		final StringBuffer sql_bf = new StringBuffer("SELECT ");
		if ("*".equals(field) || MCheck.isNull(field)) {sql_bf.append(" * ");} else {sql_bf.append(field);}
		sql_bf.append(" FROM ").append(table);
		if (!MCheck.isNull(where)) { sql_bf.append("	WHERE ").append(where);}
		if (!MCheck.isNull(params)) {
			sql_bf.append(" ").append(params[0]);
//			sql_bf.append(" order by id desc ");// order by id desc
		}
		List<MRecord> data = null;
		if (MCache.me().isEnabled()) {
			data = MCache.me().read(sql_bf.toString());
		}
		if(MCheck.isNull(data)){
			data=select(sql_bf.toString());
		}else {
			MSee.ok_sql(new IAtom() {
				public void run() throws Exception {
					MReport.doFileReport(sql_bf.toString());
				}
			}, is_dev());
		}
		return data;
	}

	public int update(final String sql, final Object... params){
		int reint=0;
		try{
			reint=update(getConnection(),sql,params);
		}catch(Exception e){//2013-1-8,如果此处出现异常,可能是mysql断掉了原因,所在需要在这重新连接一下
			connection = PoolFactory.getConnection();
			reint=update(connection,sql,params);
		}
		return reint;
	}
	public int update(final Connection conn,final String sql, final Object... params) {
		MSee.ok_sql(new IAtom() {
			public void run() throws Exception {
				Log.me().write_log(LogEnum.DEBUG, "〖更新或删除〗SQL:" + sql);
			}
		}, is_show_sql());
		MSee.ok_sql(new IAtom() {
			public void run() throws Exception {
				MReport.doSQLReport(sql, params);
			}
		}, is_dev());
		int i = 0;
		try {
			i = MSee.ok_time(new IAtom4() {
				public int run() throws Exception {
					AsyncQueryRunner query = new AsyncQueryRunner(Executors.newCachedThreadPool());
					return query.update(conn, sql, params).get();
				}
			}, is_run_time());

			if (MCache.me().isEnabled()) {
				if (i > 0) {
					MSee.ok_time(new IAtom() {
						public void run() throws Exception {
							MCache.me().delete_all();
						}
					}, is_run_time());
				}
			}
		} catch (Exception e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
		}
		return i;
	}

	public String getSQLVersion() {
		String str = null;
		try {
			str = getConnection().getMetaData().getDatabaseProductVersion();
		} catch (SQLException e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
		}
		return str;
	}

	public int insert(String table, MRecord record) {
		return insert(getConnection(),table,record);
	}
	public int insert(Connection conn,String table, MRecord record) {
		StringBuffer sql = new StringBuffer("INSERT INTO " + table);
		StringBuffer key_bf = new StringBuffer("(");
		StringBuffer value_bf = new StringBuffer("(");
		for (Map.Entry<String, Object> entry : record.entrySet()) {
			key_bf.append(entry.getKey()).append(",");
			value_bf.append("'" + entry.getValue() + "'").append(",");
		}
		String key = key_bf.substring(0, key_bf.toString().lastIndexOf(","))+ ")"; // 插入的字段名
		String value = value_bf.substring(0, value_bf.toString().lastIndexOf(","))+ ")";// 插入的字段值
		String sql_str = sql.append(key).append(" VALUES ").append(value).toString();
		int reInt=0;
		try{
			reInt=update(conn,sql_str);
		}catch(Exception e){
			conn=PoolFactory.getConnection();
			reInt=update(conn,sql_str);
		}
		return reInt;
	}

	public void insert_batch(String sql, Object[][] params) {
		try {
			AsyncQueryRunner query = new AsyncQueryRunner(Executors.newCachedThreadPool());
			query.batch(sql, params);
		} catch (SQLException e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
		}
	}

	public boolean isConnect() {
		boolean bool = false;
		if (!MCheck.isNull(getConnection())) {
			bool = true;
		}
		return bool;
	}

	public MRecord one_s(String sql, Object... params) {
		List<MRecord> lst = select(sql, params);
		return MCheck.isNull(lst) ? null : lst.get(0);
	}

	public MRecord one_t(String table, String where, String field,boolean cache,Object... params) {
		return select_one(table, where, field, cache,params);
	}

	public abstract List<MRecord> pager(String table, String where,String field,
			int current, int pageSize, Object... params);

	public abstract List<MRecord> pager_sql(String sql, int current, int pageSize,
			Object... params);

	public abstract MRecord select_one(String table, String where,String field,boolean cache, Object... params);
	
	public int update(String table, MRecord record, String where) {
		return update(getConnection(), table,record,where);
	}
	public int update(Connection conn,String table, MRecord record, String where) {
		StringBuffer sql = new StringBuffer("UPDATE ").append(table).append(" SET ");
		StringBuffer up_bf = new StringBuffer();
		// 保存语句
		for (Map.Entry<String, Object> entry : record.entrySet()) {
			up_bf.append(" ").append(entry.getKey()).append(" ").append(" = ")
					.append("'" + entry.getValue() + "'").append(",");
		}
		String up_str = up_bf.substring(0, up_bf.toString().lastIndexOf(","));
		sql = sql.append(up_str);
		// where 语句
		if (!MCheck.isNull(where)) {
			sql = sql.append(" WHERE " + where);
		}
		int reInt=0;
		try{
			reInt=update(conn,sql.toString());
		}catch(Exception e){
			conn=PoolFactory.getConnection();
			reInt=update(conn,sql.toString());
		}
		return reInt;
	}
	public int delete(String table, String where) {
		return delete(getConnection(), table, where);
	}
	public int delete(Connection conn,String table, String where) {
		StringBuffer sql = new StringBuffer("DELETE FROM ").append(table).append(" WHERE ").append(where);
		int reInt=0;
		try{
			reInt=update(conn,sql.toString());
		}catch(Exception e){
			conn=PoolFactory.getConnection();
			reInt=update(conn,sql.toString());
		}
		return reInt;
	}

	public boolean is_show_sql() {
		return MPro.me().getBool("show_sql");
	}

	public boolean is_run_time() {
		return MPro.me().getBool("show_run_time");
	}

	public boolean is_dev() {
		return MPro.me().getBool("is_dev");
	}

	public synchronized  boolean tx(ITX tx) {
		Connection conn = getConnection();
		Boolean autoCommit = null;
		boolean result = false;
		try {
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			result = tx.run();
			if (result){conn.commit();}else{conn.rollback();};
		} catch (Exception e) {
			if (conn != null)
				try {conn.rollback();} catch (Exception e1) {e1.printStackTrace();}
		} finally {
			try {
				if (conn != null) {
					if (autoCommit != null)
						conn.setAutoCommit(autoCommit);
				}
			} catch (Exception e) {
				e.printStackTrace();	// can not throw exception here, otherwise the more important exception in previous catch block can not be thrown
			}
		}
		return result;
	}
	public interface ITX {
		boolean run() throws SQLException;
	}

}
