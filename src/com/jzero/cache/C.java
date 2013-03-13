
package com.jzero.cache;

import java.util.List;
import java.util.Map;

import com.jzero.log.Log;
import com.jzero.util.MCheck;
import com.jzero.util.MPrint;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;

/** 
 * 2012-10-7: 缓存管理类
 * wangujqw@gmail.com
 */
public class C {
 
	/**
	 * 设置缓存 2011-11-17,设置缓存时间为三分钟
	 */
	public static void setCache(List<MRecord> lst, String cacheKey) {
		try {
			String cache_time=MPro.me().getStr("cache_time");
			int time=MCheck.isNull(cache_time)?3:Integer.parseInt(cache_time);
			CacheManager.me().putContent(cacheKey, lst, time * 60);
		} catch (Exception e) {
			Log.me().write_error(e);
		}
	}
	public static void setCache(Object obj,String cacheKey){
		try {
			String cache_time=MPro.me().getStr("cache_time");
			int time=MCheck.isNull(cache_time)?3:Integer.parseInt(cache_time);
			CacheManager.me().putContent(cacheKey, obj, time * 60);
		} catch (Exception e) {
			Log.me().write_error(e);
		}		
	}

	/**
	 * 
	 * @param lst 缓存的列表内容 
	 * @param cacheKey 缓存的主键
	 * @param min 时间,分为单位
	 */
	public static void setCache(List<MRecord> lst, String cacheKey,int min) {// min 分钟　
		try {
			CacheManager.me().putContent(cacheKey, lst, min * 60);
		} catch (Exception e) {
			Log.me().write_error(e);
		}
	}
	public static void setCache(Object obj, String cacheKey,int min) {// min 分钟　
		try {
			CacheManager.me().putContent(cacheKey, obj, min * 60);
		} catch (Exception e) {
			Log.me().write_error(e);
		}
	}
	/**
	 * 
	 * @param cacheKey 读取缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<MRecord> getCache(String cacheKey) {
		try {
			Cache cache=CacheManager.me().getContent(cacheKey);
			return MCheck.isNull(cache)?null:(List<MRecord>)cache.getValue();
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		return null;
	}
	public static Object getCacheObj(String cacheKey) {
		try {
			Cache cache=CacheManager.me().getContent(cacheKey);
			return MCheck.isNull(cache)?null:cache.getValue();
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		return null;
	}
	//删除
	public static void removeCache(String cacheKey) {
		try {
			CacheManager.me().invalidate(cacheKey);
		} catch (Exception e) {
			Log.me().write_error(e);
		}
	}
	public static Map<String,Cache> get(){
		return CacheManager.me().getMap();
	}
	public static void main(String[] args) throws InterruptedException {
		MPrint.print(CacheManager.me().toString());
		C.setCache("A", "a");
		C.setCache("B", "b");
		C.setCache("C", "c");
		C.setCache("D", "d");
		C.setCache("E", "e");
		C.setCache("F", "f");
		C.setCache("G", "g");
		MPrint.print(CacheManager.me().toString());
		Thread.sleep(5000*100);
	}
	
}
