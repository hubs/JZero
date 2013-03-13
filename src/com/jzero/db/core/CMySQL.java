package com.jzero.db.core;

import java.util.List;

import com.jzero.core.MReport;
import com.jzero.db.cache.MCache;
import com.jzero.db.utility.MysqlDb;
import com.jzero.util.MCheck;
import com.jzero.util.MRecord;
import com.jzero.util.MSee;
import com.jzero.util.MSee.IAtom;

/**
 * 2012-10-3: wangujqw@gmail.com
 */
public class CMySQL extends CBase {

	public List<MRecord> pager(String table, String where,String field, int current,int pageSize, Object... params) {
		final String sql = MysqlDb.getIns().get_pager_sql(table, where,field, current,pageSize, params);
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

	public List<MRecord> pager_sql(String sql, int current, int pageSize,Object... params) {
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

	@Override
	public MRecord select_one(String table, String where, String field, boolean cache,Object... params) {
		final StringBuffer sql_bf = new StringBuffer("SELECT ");
		if ("*".equals(field) || MCheck.isNull(field)) {sql_bf.append(" * ");
		} else {sql_bf.append(field);}
		sql_bf.append(" FROM ").append(table);
		if (!MCheck.isNull(where)) { // 不为空,则有where　语句
			sql_bf.append("	WHERE 1=1 ").append(where);
		}

		//	sql_bf.append("  order by id desc ");//} else {
		if (!MCheck.isNull(params)) {
			sql_bf.append(" ").append(params[0]);
		}
		sql_bf.append(" limit 1");
		if(cache){
			List<MRecord> data = null;//select(sql_bf.toString());
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


}
