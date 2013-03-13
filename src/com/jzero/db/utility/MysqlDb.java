package com.jzero.db.utility;

import java.util.LinkedList;
import java.util.List;

import com.jzero.core.MReport;
import com.jzero.db.cache.MCache;
import com.jzero.db.core.M;
import com.jzero.util.MCheck;
import com.jzero.util.MRecord;
import com.jzero.util.MSee;
import com.jzero.util.MySQL;
import com.jzero.util.MSee.IAtom;

/**
 * --- 2012-4-9 --- 
 * --- Administrator --- 
 * MysqlDb.java :操作mysql操作
 */
public final class MysqlDb implements MFieldDb {
	private MysqlDb(){}
	private static MFieldDb db=new MysqlDb();
	
	public List<MRecord> getAllTable() {
		List<MRecord> inlistMap = M.me().sql(MySQL.MYSQL_TABLE);
		List<MRecord> outList = new LinkedList<MRecord>();
		MRecord tempMap = null;
		for (MRecord r : inlistMap) {
			tempMap = new MRecord();
			tempMap.set("tablename",r.get("name"));
			tempMap.set("commentfield", r.get("comment"));
			outList.add(tempMap);
		}
		return outList;
	}

	public List<MRecord> getFieldForTable(String tableName) {
		final String sql=MySQL.MYSQL_FIELD+tableName;
		List<MRecord> data = null;
		if (MCache.me().isEnabled()) {
			data = MCache.me().read(sql);
		}
		if(MCheck.isNull(data)){
			data=M.me().sql(MySQL.MYSQL_FIELD+tableName);
		}else {
			MSee.ok_sql(new IAtom() {
				public void run() throws Exception {
					MReport.doFileReport(sql);
				}
			}, MCache.me().isEnabled());
		}
		return data;
	}
	/**
	 * defaultname格式为:display_xxx
	 * 暂定义可输入项为: display:显示此字段, 
	 * 					text:文本框
	 * 					date:时间
	 * 					datetime:时间分秒 
	 * 					select(怎样传递进来?): 
	 * 					int:只能录入整数
	 */
	public List<MRecord> getCommentAndFieldName(String tableName) {
		List<MRecord> inlistMap = getFieldForTable(tableName);
		List<MRecord> outList = new LinkedList<MRecord>();
		MRecord tempMap = null;

		for (MRecord r : inlistMap) {
			String defaultName =r.getStr("default");
			if (checkIsDisplay(defaultName)) {
				tempMap = new MRecord();
				tempMap.set("field", r.get("field"));
				tempMap.set("comment", r.get("comment"));
				outList.add(tempMap);
			}
		}
		return outList;
	}

	

	/**
	 * 检查默认字段为:display字段,
	 * 如果字段为display为返回true,如果字段类型为display_xxx,返回true,否则返回false
	 * ;
	 */
	private boolean checkIsDisplay(String defaultName) {
		if (!MCheck.isNull(defaultName)) {// 不为空
			if ("display".equalsIgnoreCase(defaultName)) {// 只显示display的字段
				return true;
			}
			String objs[] = defaultName.split("_");// eg:display_xx_dd
			if (objs.length > 0) {
				if (objs[0].equals("display")) {
					return true;
				}
			}
		}
		return false;
	}

	public List<MRecord> getDefaultAndFieldName(String tableName) {
		List<MRecord> inlistMap = getFieldForTable(tableName);
		List<MRecord> outList = new LinkedList<MRecord>();
		MRecord tempMap = null;

		for (MRecord r : inlistMap) {
			String comment = r.get("comment");
			if (!MCheck.isNull(comment)) {// 过滤掉没有注释的字段
				tempMap = new MRecord();
				tempMap.set("field", r.get("field"));
				tempMap.set("comment", comment);
				outList.add(tempMap);
			}
		}
		return outList;
	}

	public List<MRecord> getSelectField(String tableName) {
		List<MRecord> inlistMap = getFieldForTable(tableName);
		List<MRecord> outList = new LinkedList<MRecord>();
		MRecord tempMap = null;

		for (MRecord r : inlistMap) {
			String defaultName = r.getStr("default");
			if (checkIsDisplay(defaultName)) {
				tempMap = getTypeAndLenthByDefault(r.getStr("type"));
				outList.add(tempMap);
			}
		}
		return outList;
	}

	// mysql返回的类型为Type:int(11) Or datetime
	private MRecord getTypeAndLenthByDefault(String type) {
		MRecord tempMap = new MRecord();
		if (type.contains("(")) {// 有长度,eg:varchar(50);
			tempMap.set("type", type.substring(0, type.indexOf("(")));// varchar
			tempMap.set("length", type.substring(type.indexOf("(") + 1, type.lastIndexOf(")")));// 50
		} else {// 无长度,eg:datetime
			tempMap.set("type", type);
			tempMap.set("length", 0);
		}
		return tempMap;

	}

	public String get_pager_sql(String table, String where,String field, int current,
			int pageSize, Object... obj) {
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
			//sql_bf.append(" order by id desc");
		} 
		current=current<0?0:current;
		sql_bf.append(" LIMIT ").append(current).append(" , ").append(pageSize);
		return sql_bf.toString();
	}
	public static MFieldDb getIns() {
		return db;
	}
//	public static void main(String[] args) {
//		String str = "int(11)";
//		PrintUtil.print(str.contains("("));
//		PrintUtil.print(str.substring(0, str.indexOf("(")));
//		PrintUtil.print(str.substring(str.indexOf("(") + 1, str
//				.lastIndexOf(")")));
//	}



}
