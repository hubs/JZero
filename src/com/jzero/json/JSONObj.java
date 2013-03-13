package com.jzero.json;

import java.util.ArrayList;
import java.util.List;

import com.jzero.util.MCheck;
import com.jzero.util.MRecord;

/**
 * --- 2012-5-8 --- --- Administrator --- 
 * JsonObj.java :来源于json-simple:http://code.google.com/p/json-simple/ 
 * 增加过来只为将Oracle返回的字段弄成小写
 */
@SuppressWarnings("unchecked")
public class JSONObj {
	
	public static String toJSONString(Object value) {	
		if (value instanceof List)
			return JSONTool.toJSONString((List) value);
		if (value instanceof MRecord)
			return JSONTool.toJSONString((MRecord) value);

		if (value == null)
			return "\"\"";
		if (value instanceof Double) {
			if ((((Double) value).isInfinite()) || (((Double) value).isNaN())) {
				return "null";
			}
			return value.toString();
		}

		if (value instanceof Float) {
			if ((((Float) value).isInfinite()) || (((Float) value).isNaN())) {
				return "null";
			}
			return value.toString();
		}

		if (value instanceof Number) {
			return value.toString();
		}
		if (value instanceof Boolean) {
			return value.toString();
		}

		if (value instanceof String) {
			if (MCheck.isNull(value)) {
				return "\"\" ";
			}
			if ("null".equals(value)) {
				return "\"\"";
			}
			return "\"" + JSONTool.escape((String) value) + "\"";
		}

		
	
	
		return value.toString();
	}

	public static MRecord parseJson(String json) {
		MRecord reMap = new MRecord();
		String strs[] = json.replace("{", "").replace("}", "").replace("\"", "").split(",");
		String tempStr[] = null;
		for (String str : strs) {
			//2012-11-15:如果是http://格式,则只split第一个:号
			if(str.matches("^\\w*:\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")||str.contains("http://")){//避免把时间格式 yyyy-mm-dd hh:mm:ss中的hh:mm:ss拆分
				String s1=str.substring(0,str.indexOf(":"));
				String s2=str.substring(str.indexOf(":")+1);
				reMap.set(s1, s2);
			}else{
				tempStr = str.split(":");
				if (tempStr.length > 1) {
					reMap.set(tempStr[0], tempStr[1]);
				}
			}
			
		}
		return reMap;
	}
	public static List<MRecord> parseList(String json){
		List<MRecord> lst=new ArrayList<MRecord>();
		String ss[] = json.replace("[", "").replace("]", "").replace("\"", "").split("},");
		if(!MCheck.isNull(ss)){
			for(String s:ss){
				MRecord record=JSONObj.parseJson(s);
				lst.add(record);
			}
		}
		return lst;
	}

}
