package com.jzero.json;

import java.util.ArrayList;
import java.util.List;

import com.jzero.util.MPrint;
import com.jzero.util.MRecord;

/**
 * --- 2012-5-8 --- --- Administrator --- Test.java :
 */
public class Test {

	public static void main(String[] args) {
		tree();
	}
	public static void tree(){
	}
	public static void test1(){
		List<MRecord> lst=init();
		String jsonString = JSONObj.toJSONString(lst);
		MPrint.print(jsonString);
		MRecord mRecord=JSONObj.parseJson(jsonString);
		MPrint.print(mRecord);
		String json="{\"crcode\":\"MS0001456\",\"psname\":\"冯兆云\",\"sex\":\"男\",\"tel\":\"\",\"insname\":\"大额救助\",\"Topsrate\":0,\"CrState\":\"退保\"}";
		MRecord obj2=JSONObj.parseJson(json);
		MPrint.print(obj2);
		MPrint.print(obj2.get("psname"));
		MPrint.print(obj2.get("sex"));
		MRecord obj=init2();
		MPrint.print(obj);
		List<MRecord> lobj=init();
		MPrint.print(lobj);
	}
	public static void test2(){
		String sql="[{\"six\":\"15.0\",\"one\":\"1\",\"two\":\"2\",\"three\":\"3\",\"four\":\"4\",\"five\":\"5\"},{\"six\":\"15.0\",\"one\":\"1\",\"two\":\"2\",\"three\":\"3\",\"four\":\"4\",\"five\":\"5\"}]";
		String ss[] = sql.replace("[", "").replace("]", "").replace("\"", "").split("},");
		List<MRecord> records=new ArrayList<MRecord>();
		for(String s:ss){
			MRecord record=JSONObj.parseJson(s);
			records.add(record);
		}
		MPrint.print(records);
	}
	//jiema
	public static MRecord parse(String json){
		MRecord reMap=new MRecord();
		
		return reMap;
	}
	public static MRecord init2() {
		MRecord obj = new MRecord();
		obj.set("ONE", "1");
		obj.set("TWO", "2");
		obj.set("THREE", "3");
		obj.set("FOUR", 4);
		obj.set("FIVE", 5);
		obj.set("Six", Double.parseDouble("15"));
		return obj;
	}

	public static List<MRecord> init() {
		List<MRecord> lst = new ArrayList<MRecord>();
		MRecord obj =new  MRecord();
		obj.set("ONE", 1);
		obj.set("TWO", 2);
		obj.set("THREE", 3);
		obj.set("FOUR", 4);
		obj.set("FIVE", 5);
		obj.set("Six", Double.parseDouble("15"));
		lst.add(obj);
		obj =new  MRecord();
		obj.set("ONE", 1);
		obj.set("TWO", 2);
		obj.set("THREE", 3);
		obj.set("FOUR", 4);
		obj.set("FIVE", 5);
		obj.set("Six", Double.parseDouble("15"));
		lst.add(obj);
		return lst;
	}
	
	
}
