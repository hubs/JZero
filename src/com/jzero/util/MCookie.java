
package com.jzero.util;

import javax.servlet.http.Cookie;

import com.jzero.core.MInit;

/** 2012-10-3 */
public class MCookie {
	
	private static MCookie cookie=new MCookie();
	
	private MCookie(){}
	public static MCookie me(){
		return cookie;
	}
	/**
	 * Get cookie value by cookie name.
	 */
	public String getCookie(String name, String defaultValue) {
		Cookie cookie = getCookieObject(name);
		return cookie != null ? cookie.getValue() : defaultValue;
	}
	
	/**
	 * Get cookie value by cookie name.
	 */
	public String getCookie(String name) {
		return getCookie(name, null);
	}
	
	/**
	 * Get cookie value by cookie name and convert to Integer.
	 */
	public Integer getCookieToInt(String name) {
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : null;
	}
	
	/**
	 * Get cookie value by cookie name and convert to Integer.
	 */
	public Integer getCookieToInt(String name, Integer defaultValue) {
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : defaultValue;
	}
	
	/**
	 * Get cookie value by cookie name and convert to Long.
	 */
	public Long getCookieToLong(String name) {
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : null;
	}
	
	/**
	 * Get cookie value by cookie name and convert to Long.
	 */
	public Long getCookieToLong(String name, Long defaultValue) {
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : defaultValue;
	}
	
	/**
	 * Get cookie object by cookie name.
	 */
	public Cookie getCookieObject(String name) {
		Cookie[] cookies = MInit.get().getCookie();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}
	
	/**
	 * Get all cookie objects.
	 */
	public Cookie[] getCookieObjects() {
		Cookie[] result =  MInit.get().getCookie();
		return result != null ? result : new Cookie[0];
	}
	
	/**
	 * Set Cookie to response.
	 */
	public MCookie setCookie(Cookie cookie) {
		MInit.get().response().addCookie(cookie);
		return this;
	}
	
	/**
	 * Set Cookie to response.
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
	 * @param path see Cookie.setPath(String)
	 */
	public MCookie setCookie(String name, String value, int maxAgeInSeconds, String path) {
		setCookie(name, value, maxAgeInSeconds, path, null);
		return this;
	}
	
	/**
	 * Set Cookie to response.
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
	 * @param path see Cookie.setPath(String)
	 * @param domain the domain name within which this cookie is visible; form is according to RFC 2109
	 */
	public MCookie setCookie(String name, String value, int maxAgeInSeconds, String path, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (!MCheck.isNull(domain))
			cookie.setDomain(domain);
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setPath(path);
		MInit.get().response().addCookie(cookie);
		return this;
	}
	
	/**
	 * Set Cookie with path = "/".
	 */
	public MCookie setCookie(String name, String value, int maxAgeInSeconds) {
		setCookie(name, value, maxAgeInSeconds, "/", null);
		return this;
	}
	
	/**
	 * Remove Cookie with path = "/".
	 */
	public MCookie removeCookie(String name) {
		setCookie(name, null, 0, "/", null);
		return this;
	}
	
	/**
	 * Remove Cookie.
	 */
	public MCookie removeCookie(String name, String path) {
		setCookie(name, null, 0, path, null);
		return this;
	}
	
	/**
	 * Remove Cookie.
	 */
	public MCookie removeCookie(String name, String path, String domain) {
		setCookie(name, null, 0, path, domain);
		return this;
	}
}
