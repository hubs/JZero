package com.jzero.db.core;

import java.sql.Connection;
import java.util.List;

import com.jzero.db.core.CBase.ITX;
import com.jzero.util.MRecord;

/**
 * 2012-10-3: wangujqw@gmail.com
 */
public interface IDb {

	Connection getConnection();

	String getSQLVersion();

	boolean isConnect();

	List<MRecord> select(String sql, Object... params);

	List<MRecord> select(String table, String where, String field,Object... params);
	MRecord select_one(String table, String where, String field,boolean cache,Object... params);

	List<MRecord> pager(String table, String where,String field, int current, int pageSize,Object... params);
	List<MRecord> pager_sql(String sql,int current,int pageSize,Object ...params);
	
	int update(String sql, Object... params);

	int update(String table, MRecord record, String where);

	void insert_batch(String sql, Object[][] params);

	int insert(String table, MRecord record);

	int delete(String table, String where);

	MRecord one_s(String sql, Object... params);

	
	boolean is_show_sql();

	boolean  tx(ITX tx);
}
