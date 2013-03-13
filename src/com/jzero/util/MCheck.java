package com.jzero.util;

import java.util.List;
@SuppressWarnings("unchecked")
public final class MCheck {
	private MCheck() {
	}

	public static boolean isNull(String obj) {
		return isNull((Object) obj);
	}

	public static boolean isNull(Object obj) {
		boolean bool = false;
		if (obj == null) {
			bool = true;
		} else {
			String str = obj.toString();
			if(str.equals("[]")){
				bool=true;
			}else if (str.equals("''")) {
				bool = true;
			} else if ("".equals(str)) {
				bool = true;
			} else if ("".equals(str.trim())) {
				bool = true;
			} else if ("null".equalsIgnoreCase(str)) {
				bool = true;
			}
		}
		return bool;
	}

	public static boolean isNull(Object... obj) {
		boolean bool = false;
		if (obj == null) {
			bool = true;
		} else if (obj.length == 0) {
			bool = true;
		}
		return bool;
	}

	public static boolean isNull(String... obj) {
		boolean bool = false;
		if (obj == null) {
			bool = true;
		} else if (obj.length == 0) {
			bool = true;
		}else if(obj[0].equals("")){
			bool=true;
		}
		return bool;
	}

	public static boolean isNull(MRecord record) {
		if (record == null) {
			return true;
		}
		return record.getColumns().isEmpty() ? true : false;
	}

	public static boolean isNull(List obj) {
		boolean bool = false;
		if (obj == null) {
			bool = true;
		} else if (obj.size() == 0) {
			bool = true;
		}
		return bool;
	}
	public static boolean isNull(Integer in){
		return in==null?true:in==0?true:false;
	}
}