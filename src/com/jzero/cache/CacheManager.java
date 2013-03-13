package com.jzero.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
	private CacheManager() {
	}

	private static CacheManager instanct = new CacheManager();
	private static Map<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

	public static CacheManager me() {
		return instanct;
	}

	public Map<String, Cache> getMap() {
		return cacheMap;
	}

	/**
	 * returns cache item from hashmap
	 * 
	 * @param key
	 * @return Cache
	 */
	private Cache getCache(String key) {
		return cacheMap.get(key);
	}

	/**
	 * Looks at the hashmap if a cache item exists or not
	 * 
	 * @param key
	 * @return Cache
	 */
	private boolean hasCache(String key) {
		return cacheMap.containsKey(key);
	}

	/**
	 * Invalidates all cache
	 */
	public void invalidateAll() {
		cacheMap.clear();
	}

	/**
	 * Invalidates a single cache item
	 * 
	 * @param key
	 */
	public void invalidate(String key) {
		cacheMap.remove(key);
	}

	/**
	 * Adds new item to cache hashmap
	 * 
	 * @param key
	 * @return Cache
	 */
	private void putCache(String key, Cache object) {
		cacheMap.put(key, object);
	}
	public int getSize() {
        return cacheMap.size();
    }
	/**
	 * Reads a cache item's content
	 * 
	 * @param key
	 * @return
	 */
	public Cache getContent(String key) {
		if (hasCache(key)) {
			Cache cache = getCache(key);
			if (cache.isExpired()) {
				return null;
			}
			if (cacheExpired(cache)) {
				cache.setExpired(true);
			}
			return cache;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param key
	 * @param content
	 * @param ttl
	 */
	public void putContent(String key, Object content, long ttl) {
		Cache cache = new Cache();
		cache.setKey(key);
		cache.setValue(content);
		cache.setTimeOut(ttl * 1000 + new Date().getTime());
		cache.setExpired(false);
		putCache(key, cache);
	}

	/** @modelguid {172828D6-3AB2-46C4-96E2-E72B34264031} */
	private boolean cacheExpired(Cache cache) {
		if (cache == null) {
			return false;
		}
		long milisNow = new Date().getTime();
		long milisExpire = cache.getTimeOut();
		if (milisExpire < 0) { // Cache never expires
			return false;
		} else if (milisNow >= milisExpire) {
			return true;
		} else {
			return false;
		}
	}

}
