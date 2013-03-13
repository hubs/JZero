package com.jzero.json;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jzero.util.MCheck;
import com.jzero.util.MRecord;

/**
 * --- 2012-5-8 --- 
 * 来源于json-simple: http://code.google.com/p/json-simple/ 
 */
@SuppressWarnings("unchecked")
public class JSONTool {
	
	
	public static String toJSONString(List list) {
		if (list == null)
			return "null";

		boolean first = true;
		StringBuffer sb = new StringBuffer();
		Iterator iter = list.iterator();

		sb.append('[');
		while (iter.hasNext()) {
			if (first)
				first = false;
			else
				sb.append(',');

			Object value = iter.next();
			if (value == null) {
				sb.append("null");
				continue;
			}
			sb.append(JSONObj.toJSONString(value));
		}
		sb.append(']');
		return sb.toString().trim();
	}

	public static String toJSONString(MRecord record) {
		if (record == null)
			return "null";

		StringBuffer sb = new StringBuffer();
		boolean first = true;
		Iterator<Entry<String, Object>> iter = record.entrySet().iterator();

		sb.append('{');
		while (iter.hasNext()) {
			if (first)
				first = false;
			else
				sb.append(',');
			Map.Entry entry =  iter.next();
			String name=String.valueOf(entry.getKey());
			Object value=entry.getValue();
			toJSONString(name.toLowerCase(),value, sb);
		}
		sb.append('}');
		return sb.toString();
	}

	private static String toJSONString(String key, Object value, StringBuffer sb) {
		sb.append('\"');
		if (key == null)
			sb.append("null");
		else
			escape(key, sb);
		sb.append('\"').append(':');

		sb.append(JSONObj.toJSONString(value));

		return sb.toString();
	}
	public static String escape(String s){
		if(MCheck.isNull(s))
			return "";
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }
	static void escape(String s, StringBuffer sb) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
//			case '/':
//				sb.append("\\/");
//				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}// for
	}
}
