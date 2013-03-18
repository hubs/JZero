package com.jzero.db.utility;

import java.util.List;

import com.jzero.util.MCheck;
import com.jzero.util.MRecord;

/**
 * 2013-3-15:
 * sqlite 嵌入式数据库,不般不用于后以操作,所以此处不再进其进行表取数据
 * sqlite的主表是　sqlite_master,里面没有mysql的Comment注释,所以不好取,因此放弃
 */
public final class MSqlite implements MFieldDb {
	private MSqlite() {
	}

	private static MFieldDb db = new MSqlite();

	public List<MRecord> getAllTable() {
		return null;
	}

	public List<MRecord> getCommentAndFieldName(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MRecord> getDefaultAndFieldName(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MRecord> getFieldForTable(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MRecord> getSelectField(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String get_pager_sql(String table, String where, String field,
			int current, int pageSize, Object... obj) {
		StringBuffer sql_bf = new StringBuffer();
		if(MCheck.isNull(field)){
			sql_bf.append(" SELECT * FROM ").append(table);
		}else{
			sql_bf.append(" SELECT ").append(field).append(" FROM ").append(table);
		}
		if (!MCheck.isNull(where)) { // 不为空,则有where　语句
			sql_bf.append(" WHERE 1=1 ").append(where);
		}
		if (!MCheck.isNull(obj)) {
			sql_bf.append(" ").append(obj[0]);
		} 
		current=current<0?0:current;
		sql_bf.append(" LIMIT ").append(current).append(" , ").append(pageSize);
		return sql_bf.toString();
	}

	public static MFieldDb getIns() {
		return db;
	}
}
