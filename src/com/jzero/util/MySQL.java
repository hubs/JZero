
package com.jzero.util;
/**
 * --- 2012-4-12 ---
 * --- Administrator ---
 * MySQL.java : 存放自定义SQL
 */
public class MySQL {
	
	/*************************************MSSQL读取字段属性SQL开始**********************************/
	/**
	 * --- 2012-4-9 --- 
	 * --- Administrator --- 
	 * MssqlDb.java : 针对Mssql操作
	 * 取出的数据为:表名、字段名称、字段长度、字段类型、默认值、描述
	 * 1、从Sysobject 中取出表名 (id)
	 * 2、从Systeproperties:取出描述字段(id)(smallid=>colid)
	 * 3、从syscolumns:取出字段名称与长度(id)(cdefault)(xtype)(colid)
	 * 4、从syscomments:取默认值(id=>cdefault) 
	 * 5、从systypes：取字段类型(xtype)
	 */
	public static final String MSSQL_FIELD = "select "
		+ "a.name as tablename,a.id,"
		+ "b.name as fieldname,b.length as length,b.colid ,"
		+ "c.name as typename,"
		+ "d.text as defaultname ,"
		+ "e.value as commentname"
		+ "  from sysobjects as a"
		+ "  left join syscolumns as b on a.id=b.id "
		+ "  left join systypes as c on b.xtype=c.xtype"
		+ "  left join syscomments as d on b.cdefault=d.id"
		+ "  left join sysproperties as e on b.colid=e.smallid and a.id=e.id"
		+ " where a.xtype='U' and a.name=?";

		// 只读取表名与表注释,未注释的不读出来
	public static final String MSSQL_TABLE = "select b.name tablename,a.value commentfield from sysproperties a "
				+ " left join sysobjects b on a.id=b.id"
				+ " where a.type='3' -- 3是表名,4是表字段名";// 取表名
		
	/*************************************MSSQL读取字段属性SQL结束**********************************/
		
	/*************************************MYSQL读取字段属性SQL开始**********************************/
	// 读取所有的表
	public static final String MYSQL_TABLE = "SHOW TABLE STATUS";

	// 读取某一个表的字段
	public static final String MYSQL_FIELD = "SHOW  FULL FIELDS FROM ";
	/*************************************MYSQL读取字段属性SQL结束**********************************/
	
	
	/*************************************SQLITE读取字段属性SQL开始**********************************/
	// 读取所有的表
	public static final String SQLITE_TABLE = "SELECT * FROM sqlite_master";

	/*************************************SQLITE读取字段属性SQL结束**********************************/
}
