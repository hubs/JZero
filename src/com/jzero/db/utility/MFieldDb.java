
package com.jzero.db.utility;

import java.util.List;

import com.jzero.util.MRecord;

/**
 * --- 2012-4-9 ---
 * DbField.java : 用于操作指定表中的表字段。
 */
public interface MFieldDb {
	
	/**
	 *得到数据库中的所有的表
	 */
	List<MRecord>  getAllTable();
	
	/**
	 * 得到指定表中的字段,getCommentAndFieldName(),getDefaultAndFieldName()从此方法返回的值取数据。
	 */
	List<MRecord> getFieldForTable(String tableName);

	/**
	 * 得到表结构中的字段名与备注信息
	 */
	List<MRecord> getCommentAndFieldName(String tableName);
	
	/**
	 * 得到表结构中的默认值与备注信息
	 */
	List<MRecord> getDefaultAndFieldName(String tableName);

	/**
	 * 得到表中的默认值,备注信息与表字段
	 * 默认值用来判断是什么类型(text,data,select...准备在高搜索中使用)
	 */
	List<MRecord>  getSelectField(String tableName);
	
	String get_pager_sql(String table, String where,String field, int current,int pageSize, Object... obj);
	

}
