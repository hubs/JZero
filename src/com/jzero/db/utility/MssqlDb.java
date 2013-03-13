package com.jzero.db.utility;

import java.util.LinkedList;
import java.util.List;

import com.jzero.db.core.M;
import com.jzero.util.MCheck;
import com.jzero.util.MRecord;
import com.jzero.util.MySQL;


public final class MssqlDb implements MFieldDb {
	private static MFieldDb db=new MssqlDb();
	private MssqlDb(){}
	public List<MRecord> getFieldForTable(String tableName) {
		return M.me().sql(MySQL.MSSQL_FIELD, tableName);
	}

	/**
	 * 得到数据库中的所有的表
	 */
	public List<MRecord> getAllTable() {
		return M.me().sql(MySQL.MSSQL_TABLE);
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
			String defaultName =r.getStr("defaultname");
			if(checkIsDisplay(defaultName)){
				tempMap = new MRecord();
				tempMap.set("field",r.get("fieldname"));
				tempMap.set("comment", r.get("commentname"));
				outList.add(tempMap);
			}
		}
		return outList;
	}
	/**
	 * 检查默认字段为:display字段,如果字段为display为返回true,如果字段类型为display_xxx,返回true,否则返回false;
	 */
	private boolean checkIsDisplay(String defaultName){
		if(!MCheck.isNull(defaultName)){//不为空
			if ("display".equalsIgnoreCase(defaultName)) {// 只显示display的字段
				return true;
			}
			String objs[]=defaultName.split("_");//eg:display_xx_dd
			if(objs.length>0){
				if(objs[0].equals(defaultName)){
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
			String comment = r.getStr("commentname");
			if (!MCheck.isNull(comment)) {// 过滤掉没有注释的字段
				tempMap = new MRecord();
				tempMap.set("field", r.get("fieldname"));
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
			String defaultName =r.get("defaultname");
			if(checkIsDisplay(defaultName)){
				tempMap=getTypeAndLenthByDefault(defaultName,r.get("length"));
				outList.add(tempMap);
			}
		}
		return outList;
	}
	//返回 类型与长度,格式:display_xx
	private MRecord getTypeAndLenthByDefault(String defaultName,Object length) {
		MRecord tempMap = new MRecord();
		String objs[]=defaultName.split("_");
		if(objs.length>1){
			tempMap.set("type", objs[1]);
		}
		tempMap.set("length",length);
		return tempMap;
		
	}
	public static MFieldDb getIns() {
		return db;
	}
	/**
	 *返回分页界面 current:当前第几页 pageSize:每页显示的数量,MSSQL暂时还未测试
	 */
	public String get_pager_sql(String table, String where,String field,int current, int pageSize, Object... obj) {
		StringBuffer sql_bf = new StringBuffer("SELECT TOP ").append(pageSize).append(" * FROM ").append(table)
		.append(" WHERE id NOT IN (SELECT TOP ").append(current*pageSize).append(" id FROM ").append(table)
		.append(" ORDER BY ID DESC )");
		if (!MCheck.isNull(where)) { // 不为空,则有where　语句
			sql_bf.append(" AND  ").append(where);
		}
		if (MCheck.isNull(obj)) {
			sql_bf.append(" order by id desc");
		} else {
			sql_bf.append(" ").append(obj);
		}
		return sql_bf.toString();
	}
	
	
}
