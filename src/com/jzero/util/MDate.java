package com.jzero.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.jzero.log.Log;
import com.jzero.log.LogEnum;

/** 2012-10-3 */
public class MDate {

	public static SimpleDateFormat getSimpleDateFormat(String pattStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattStr);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf;
	}

	public static String get_ymd_hms() {
		return getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate());
	}
	public static String get_ymd_hms_join() {
		return getSimpleDateFormat("yyyyMMddHHmmss").format(newDate());
	}
	public static String get_ymd() {
		return getSimpleDateFormat("yyyy-MM-dd").format(newDate());
	}

	public static String get_year() {
		return getSimpleDateFormat("yyyy").format(newDate());
	}

	public static String get_month() {
		return getSimpleDateFormat("MM").format(newDate());
	}

	public static String get_day() {
		return getSimpleDateFormat("dd").format(newDate());
	}

	public static String get_ymd_hm() {
		return getSimpleDateFormat("yyyy-MM-dd HH:mm").format(newDate());
	}

	public static String get_after_minute(int minute) {
		SimpleDateFormat sdf = getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar afterTime = Calendar.getInstance();
		afterTime.add(12, minute);
		afterTime.set(13, 0);
		return sdf.format(afterTime.getTime());
	}

	// 当前系统前10分钟
	public static Date get_pre_Ten_min() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, Calendar.SECOND - 10);
		return cal.getTime();
	}

	// 指定多少分钟后的时间
	public static Date get_after_minute_date(int minute) {
		Calendar afterTime = Calendar.getInstance();
		afterTime.add(Calendar.MINUTE, minute);
		return afterTime.getTime();
	}

	// 指定多少毫秒后的时间
	public static Date get_after_milliscond(int scond) {
		Calendar afterTime = Calendar.getInstance();
		afterTime.add(Calendar.MILLISECOND, scond);
		return afterTime.getTime();
	}

	public static String get_ym() {
		return getSimpleDateFormat("yyyy-MM").format(newDate());
	}

	public static java.util.Date newDate() {
		return new java.util.Date();
	}

	public static java.util.Date toYMD(String str) {
		try {
			return getSimpleDateFormat("yyyy-MM-dd").parse(str);
		} catch (ParseException e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
		}
		return newDate();
	}

	public static java.util.Date toYMD_HMS(String str) {
		try {
			return getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (ParseException e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
		}
		return newDate();
	}

	public static String get_max_day(Object time) {
		if (!(MCheck.isNull(time))) {
			String[] str = time.toString().split("-");
			if (str.length != 2) {
				return time.toString();
			}
			Calendar cal = Calendar.getInstance();
			cal.set(1, Integer.parseInt(str[0]));
			cal.set(2, Integer.parseInt(str[1]) - 1);
			int maxDate = cal.getActualMaximum(5);
			return time + "-" + maxDate;
		}
		return "";
	}

	public static Object get_min_day(Object time) {
		if (!(MCheck.isNull(time))) {
			String[] str = time.toString().split("-");
			if (str.length != 2) {
				return time;
			}
			return time + "-" + "01";
		}
		return "";
	}

	public static Object get_min_day() {
		return get_min_day(get_ym());
	}

	public static String get_max_day() {
		return get_max_day(get_ym());
	}

	public static String[] split_ym(Object time) {
		return MCheck.isNull(time)?null:time.toString().split("-");
	}
	public static int get_now_second(){
		return (int) (System.currentTimeMillis()/1000L);
	}
	public static void main(String[] args) {
		MPrint.print(System.currentTimeMillis()/1000L);
	}
	
}
