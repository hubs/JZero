package com.jzero.db.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.jzero.core.MReport;
import com.jzero.db.cache.MCache;
import com.jzero.db.utility.MSqlite;
import com.jzero.util.MCheck;
import com.jzero.util.MRecord;
import com.jzero.util.MSee;
import com.jzero.util.MSee.IAtom;

/**
 * 2013-3-14
 */
public class CSqlite  extends CBase {

	@Override
	public List<MRecord> pager(String table, String where, String field,
			int current, int pageSize, Object... params) {
		final String sql = MSqlite.getIns().get_pager_sql(table, where,field, current,pageSize, params);
		List<MRecord> data = null;
		if (MCache.me().isEnabled()) {
			data = MCache.me().read(sql);
		}
		if(MCheck.isNull(data)){
			data=select(sql);
		}else {
			MSee.ok_sql(new IAtom() {
				public void run() throws Exception {
					MReport.doFileReport(sql);
				}
			}, is_dev());
		}
		return data;
	}
	/**
	 * 2013-3-15:根mysql一致,因为他们的分页都为limit操作
	 */
	@Override
	public List<MRecord> pager_sql(String sql, int current, int pageSize,
			Object... params) {
		final StringBuffer sql_bf = new StringBuffer(sql);
		sql_bf.append(" LIMIT ").append(current).append(" , ").append(pageSize);
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
	/**
	 * 2013-3-15:根mysql一致,因为他们的分页都为limit操作
	 */
	@Override
	public MRecord select_one(String table, String where, String field,
			boolean cache, Object... params) {
		final StringBuffer sql_bf = new StringBuffer("SELECT ");
		if ("*".equals(field) || MCheck.isNull(field)) {sql_bf.append(" * ");
		} else {sql_bf.append(field);}
		sql_bf.append(" FROM ").append(table);
		if (!MCheck.isNull(where)) { // 不为空,则有where　语句
			sql_bf.append("	WHERE 1=1 ").append(where);
		}
		if (!MCheck.isNull(params)) {
			sql_bf.append(" ").append(params[0]);
		}
		sql_bf.append(" limit 1");
		if(cache){
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
			
			return MCheck.isNull(data)?null:data.get(0);
		}else{
			List<MRecord> data = select(sql_bf.toString());
			return MCheck.isNull(data)?null:data.get(0);
		}
	}
	public static void main(String[] args) {
	    Connection connection = null;
	    try{
	      Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
	      Statement statement = connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      statement.executeUpdate("drop table if exists person");
	      statement.executeUpdate("create table person (id integer, name string)");
	      statement.executeUpdate("insert into person values(1, 'leo')");
	      statement.executeUpdate("insert into person values(2, 'yui')");
	      statement.executeUpdate("backup to backup.db");
	      ResultSet rs = statement.executeQuery("select * from person");
	      while(rs.next()){
	        // read the result set
	        System.out.println("name = " + rs.getString("name"));
	        System.out.println("id = " + rs.getInt("id"));
	      }
	    }catch(Exception e){
	      System.err.println(e.getMessage());
	    }
	    finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        System.err.println(e);
	      }
	    }
	}

}
