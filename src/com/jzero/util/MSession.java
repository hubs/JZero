package com.jzero.util;

import java.util.ArrayList;
import java.util.List;

import com.jzero.core.MR;

public class MSession {

	private static List<Object> userList = new ArrayList<Object>();

	public  static void save(MRecord obj) {
		MR.me().setSessionAttr(Msg.USER_NAME, obj);
		userList.add(obj.get("xm"));
	}

	public static MRecord get() {
		MRecord record=MR.me().getSessionAttr(Msg.USER_NAME);
		return MCheck.isNull(record)?null:record;
	}
	public static  String getName(){
		return MCheck.isNull(get())?"":get().getStr("xm");
	}

	public static void clear() {
		MRecord record=get();
		if(!MCheck.isNull(record)){
			MR.me().removeSessionAttr(Msg.USER_NAME);
			if(userList.contains(record.get("xm"))){
				userList.remove(record.get("xm"));
			}
		}
		MPrint.print(record.get("xm")+" is out!");
		
	}

	public static void loopCurrUser() {
		for (Object s : userList) {
			MPrint.print("Now online user =>"+s);
		}
	}
}