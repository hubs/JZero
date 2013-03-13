package com.jzero.core;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jzero.comm.MCommHelp;
import com.jzero.log.Log;
import com.jzero.token.MToken;
import com.jzero.util.MCheck;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;

/**
 * 2012-10-3: 管理request,response,session
 *  wangujqw@gmail.com
 */
public class MR{

	private boolean global_xss;
	private String charset;
	private  HttpServletRequest request;
	private HttpServletResponse response;


	public static MR init(MInit mInit) {
		MR mr=new MR();
		mr.request = mInit.request();
		mr.response = mInit.response();
		mr.setGlobal_xss(MPro.me().getBool("global_xss_filtering"));
		mr.setCharset(mInit.getConfig().getStr("charset"));
		return mr;
	}

	public static MR me() {
		return MInit.get().getMr();
	}

	/**
	 * Stores an attribute in this request
	 * 
	 * @param name
	 *            a String specifying the name of the attribute
	 * @param value
	 *            the Object to be stored
	 */
	public MR setAttr(String name, Object value) {
		if(!MCheck.isNull(request)){request.setAttribute(name, value);}
		return this;
	}


	/**
	 * Removes an attribute from this request
	 * 
	 * @param name
	 *            a String specifying the name of the attribute to remove
	 */
	public MR removeAttr(String name) {
		request.removeAttribute(name);
		return this;
	}

	/**
	 * Stores attributes in this request, key of the map as attribute name and
	 * value of the map as attribute value
	 * 
	 * @param attrMap
	 *            key and value as attribute of the map to be stored
	 */
	public MR setAttrs(Map<String, Object> attrMap) {
		for (Map.Entry<String, Object> entry : attrMap.entrySet())
			request.setAttribute(entry.getKey(), entry.getValue());
		return this;
	}

	/**
	 * Returns the value of a request parameter as a String, or null if the
	 * parameter does not exist.
	 * <p>
	 * You should only use this method when you are sure the parameter has only
	 * one value. If the parameter might have more than one value, use
	 * getParaValues(java.lang.String).
	 * <p>
	 * If you use this method with a multivalued parameter, the value returned
	 * is equal to the first value in the array returned by getParameterValues.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @return a String representing the single value of the parameter
	 */
	public String getPara(String name) {
		String param = request.getParameter(name);
		return MCheck.isNull(param)?null:isGlobal_xss() ? MTool.removeCharacter(param) : param;
	}


	/**
	 * Returns the value of a request parameter as a String, or null if the
	 * parameter does not exist.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @param defaultValue
	 *            a String value be returned when the value of parameter is null
	 * @return a String representing the single value of the parameter
	 */
	public String getPara(String name, String defaultValue) {
		String result = getPara(name);
		return MCheck.isNull() ? defaultValue : result;
	}

	public String getProject() {
		return request.getContextPath();
	}

	/**
	 * Returns the values of the request parameters as a Map.
	 * 
	 * @return a Map contains all the parameters name and value
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParaMap() {
		Map<String, String> inMap = request.getParameterMap();
		Map<String, Object> outMap = new HashMap<String, Object>();
		if (!MCheck.isNull(inMap)) {
			for (Map.Entry<String, String> entry : inMap.entrySet()) {
				String name = entry.getKey();
				String values = getPara(name);
				if(!MCheck.isNull(values)){
					//2012-11-15 这里将内容中所有的,(逗号)转换成.(句话),原因是因为与jsonObj解析时冲突(json是根据,(逗号)进行解析!)
					values=values.replaceAll(",", ".");
					outMap.put(name, values);
				}
			}
		}
		return outMap;
	}
	/**
	 * 2012-10-11:新增时用到,页面中的值
	 */
	public MRecord getPara(){
		if(isPOST()){
			csrf_verify();
			return new MRecord().setColumns_r(getParaMap());
		}else{
			return new MRecord().setColumns_r(getParaMap());
		}
//		MB.me().getTextRender("The form submit must is post!!");
//		return null;
	}
	/**
	 * Returns an Enumeration of String objects containing the names of the
	 * parameters contained in this request. If the request has no parameters,
	 * the method returns an empty Enumeration.
	 * 
	 * @return an Enumeration of String objects, each String containing the name
	 *         of a request parameter; or an empty Enumeration if the request
	 *         has no parameters
	 */
	@SuppressWarnings("unchecked")
	public Enumeration<String> getParaNames() {
		return request.getParameterNames();
	}

	/**
	 * Returns an array of String objects containing all of the values the given
	 * request parameter has, or null if the parameter does not exist. If the
	 * parameter has a single value, the array has a length of 1.
	 * 
	 * @param name
	 *            a String containing the name of the parameter whose value is
	 *            requested
	 * @return an array of String objects containing the parameter's values
	 */
	public String[] getParaValues(String name) {
		return request.getParameterValues(name);
	}

	/**
	 * Returns an array of Integer objects containing all of the values the
	 * given request parameter has, or null if the parameter does not exist. If
	 * the parameter has a single value, the array has a length of 1.
	 * 
	 * @param name
	 *            a String containing the name of the parameter whose value is
	 *            requested
	 * @return an array of Integer objects containing the parameter's values
	 */
	public Integer[] getParaValuesToInt(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null)
			return null;
		Integer[] result = new Integer[values.length];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.parseInt(values[i]);
		return result;
	}

	/**
	 * Returns an Enumeration containing the names of the attributes available
	 * to this request. This method returns an empty Enumeration if the request
	 * has no attributes available to it.
	 * 
	 * @return an Enumeration of strings containing the names of the request's
	 *         attributes
	 */
	@SuppressWarnings("unchecked")
	public Enumeration<String> getAttrNames() {
		return request.getAttributeNames();
	}

	/**
	 * Returns the value of the named attribute as an Object, or null if no
	 * attribute of the given name exists.
	 * 
	 * @param name
	 *            a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or null if the
	 *         attribute does not exist
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttr(String name) {
		Object obj=request.getAttribute(name);
		if(MCheck.isNull(obj)){
			if(obj instanceof List){//列表
				return null;
			}else if(obj instanceof String){
				return (T) "";
			}
		}
		return (T) obj;
	}
	@SuppressWarnings("unchecked")
	public <T> T getAttr_UTF8(String name){
		return (T) MTool.UTF8(getAttr(name));
	}
//	public <T> T getPageContext(String name){
////		return (T) request.getAttribute(name,);
//	}

	/**
	 * Returns the value of the named attribute as an Object, or null if no
	 * attribute of the given name exists.
	 * 
	 * @param name
	 *            a String specifying the name of the attribute
	 * @return an String Object containing the value of the attribute, or null
	 *         if the attribute does not exist
	 */
	public String getAttrForStr(String name) {
		return (String) request.getAttribute(name);
	}

	/**
	 * Returns the value of the named attribute as an Object, or null if no
	 * attribute of the given name exists.
	 * 
	 * @param name
	 *            a String specifying the name of the attribute
	 * @return an Integer Object containing the value of the attribute, or null
	 *         if the attribute does not exist
	 */
	public Integer getAttrForInt(String name) {
		return (Integer) request.getAttribute(name);
	}

	private Integer getParaToInt_(String result, Integer defaultValue) {
		if (result == null)
			return defaultValue;
		if (result.startsWith("N") || result.startsWith("n"))
			return -Integer.parseInt(result.substring(1));
		return Integer.parseInt(result);
	}

	/**
	 * Returns the value of a request parameter and convert to Integer.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @return a Integer representing the single value of the parameter
	 */
	public Integer getParaToInt(String name) {
		return getParaToInt_(request.getParameter(name), null);
	}

	/**
	 * Returns the value of a request parameter and convert to Integer with a
	 * default value if it is null.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @return a Integer representing the single value of the parameter
	 */
	public Integer getParaToInt(String name, Integer defaultValue) {
		return getParaToInt_(request.getParameter(name), defaultValue);
	}

	private Long getParaToLong_(String result, Long defaultValue) {
		if (result == null)
			return defaultValue;
		if (result.startsWith("N") || result.startsWith("n"))
			return -Long.parseLong(result.substring(1));
		return Long.parseLong(result);
	}

	/**
	 * Returns the value of a request parameter and convert to Long.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @return a Integer representing the single value of the parameter
	 */
	public Long getParaToLong(String name) {
		return getParaToLong_(request.getParameter(name), null);
	}

	/**
	 * Returns the value of a request parameter and convert to Long with a
	 * default value if it is null.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @return a Integer representing the single value of the parameter
	 */
	public Long getParaToLong(String name, Long defaultValue) {
		return getParaToLong_(request.getParameter(name), defaultValue);
	}

	/**
	 * Returns the value of a request parameter and convert to Boolean.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @return false if the value of the parameter is "false" or "0", true if it
	 *         is "true" or "1", null if parameter is not exists
	 */
	public Boolean getParaToBoolean(String name) {
		String result = request.getParameter(name);
		if (result != null) {
			result = result.trim().toLowerCase();
			if (result.equals("1") || result.equals("true"))
				return Boolean.TRUE;
			else if (result.equals("0") || result.equals("false"))
				return Boolean.FALSE;
			// return Boolean.FALSE; // if use this, delete 2 lines code under
		}
		return null;
	}

	/**
	 * Returns the value of a request parameter and convert to Boolean with a
	 * default value if it is null.
	 * 
	 * @param name
	 *            a String specifying the name of the parameter
	 * @return false if the value of the parameter is "false" or "0", true if it
	 *         is "true" or "1", default value if it is null
	 */
	public Boolean getParaToBoolean(String name, Boolean defaultValue) {
		Boolean result = getParaToBoolean(name);
		return result != null ? result : defaultValue;
	}
	public Boolean getParaToBoolean(HttpServletRequest request,String name,boolean defaultValue){
		String bool=request.getParameter(name);
		if (bool != null) {
			bool = bool.trim().toLowerCase();
			if (bool.equals("1") || bool.equals("true"))
				return Boolean.TRUE;
			else if (bool.equals("0") || bool.equals("false"))
				return Boolean.FALSE;
		}
		return defaultValue;
	}
	@SuppressWarnings("unchecked")
	public <T>T getPara(HttpServletRequest request,String name){
		return (T) request.getParameter(name);
	}
	@SuppressWarnings("unchecked")
	public <T>T getPara(HttpServletRequest request,String name,String defaultValue){
		String param = request.getParameter(name);
		if(!MCheck.isNull(param)&&param.contains("?")){
			if (getCharset().equalsIgnoreCase("UTF-8")) {
				param = MTool.UTF8(param);
			} else if (getCharset().equalsIgnoreCase("GBK")) {
				param = MTool.GBK(param);
			}
		}
		return (T) (MCheck.isNull(param)?defaultValue:param);
	}
	/**
	 * Return HttpServletRequest. Do not use HttpServletRequest Object in
	 * constructor of MRP
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Return HttpServletResponse. Do not use HttpServletResponse Object in
	 * constructor of MRP
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Return HttpSession.
	 */
	public HttpSession getSession() {
		return getSession(false);
	}

	/**
	 * Return HttpSession.
	 * 
	 * @param create
	 *            a boolean specifying create HttpSession if it not exists
	 */
	public HttpSession getSession(boolean create) {
		HttpSession session= request.getSession(create);
		session =init(session);
		return session;
	}
	private HttpSession init(HttpSession session){
		boolean bool_browser=MPro.me().getBool("sess_match_useragent");// pattern borwser
		boolean bool_ip=MPro.me().getBool("sess_match_ip");
		if(MCheck.isNull(session)){// Create init
			session=request.getSession(true);
			session.setMaxInactiveInterval(MPro.me().getInt("sess_expiration"));
			if(bool_browser){
				session.setAttribute("browser", request.getHeader("User-Agent"));
			}
			if(bool_ip){
				session.setAttribute("ip", MTool.getIp());
			}
		}else{
			//如果不相匹配，那这里怎么处理呢?还没想好
			Object brower=session.getAttribute("browser");
			if(bool_browser){
				if(!brower.equals(request.getHeader("User-Agent"))){
					quit(session);
				}
			}
			Object ip=session.getAttribute("ip");
			if(bool_ip){
				if(!ip.equals(MTool.getIp())){
					quit(session);
				}
			}
			
		}
		return session;
	}
	private void quit(HttpSession session){
		   session.invalidate();
			String script="<script type='text/javascript'>var Sys = {};" +
					"var ua = navigator.userAgent.toLowerCase();"+
					" window.ActiveXObject ? Sys.ie = ua.match(/msie ([\\d.]+)/)[1] :"+
			        " document.getBoxObjectFor ? Sys.firefox = ua.match(/firefox\\/([\\d.]+)/)[1] :" +
			        " window.MessageEvent && !document.getBoxObjectFor ? Sys.chrome = ua.match(/chrome\\/([\\d.]+)/)[1] :" +
			        " window.opera ? Sys.opera = ua.match(/opera.([\\d.]+)/)[1] :" +
			        " window.openDatabase ? Sys.safari = ua.match(/version\\/([\\d.]+)/)[1] : 0;" +
			        "if (window.self != window.parent) {" +
				        "if(Sys.ie){ " +
				        "    window.top.location.href('"+MTool.getBase()+"');" +
				        "}else{" +
				        "    window.top.location.reload();" +
				        "}"+
				    "}"+
			   " </script>";
			MCommHelp.outHTML(script);
	}

	/**
	 * Return a Object from session.
	 * 
	 * @param key
	 *            a String specifying the key of the Object stored in session
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSessionAttr(String key) {
		HttpSession session = request.getSession(false);
		return session != null ? (T) session.getAttribute(key) : null;
	}

	/**
	 * Store Object to session.
	 * 
	 * @param key
	 *            a String specifying the key of the Object stored in session
	 * @param value
	 *            a Object specifying the value stored in session
	 */
	public MR setSessionAttr(String key, Object value) {
		request.getSession().setAttribute(key, value);
		return this;
	}

	/**
	 * Remove Object in session.
	 * 
	 * @param key
	 *            a String specifying the key of the Object stored in session
	 */
	public MR removeSessionAttr(String key) {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.removeAttribute(key);
		return this;
	}

	public String getURI() {
		return request.getRequestURI();
	}

	public String getURL() {
		return request.getRequestURL().toString();
	}

	public String getMethod() {
		return request.getMethod();
	}

	public boolean isGlobal_xss() {
		return global_xss;
	}

	public void setGlobal_xss(boolean globalXss) {
		global_xss = globalXss;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void redirect(String url) {
		url=getToURI(url);
		try {
			getResponse().sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
			Log.me().write_error(e);
		}
	}
	public String getToURI(String url){
		String project_name=getProject();
//		url="/"+MTool.firstCharToLowerCase(url);
		url="/"+url;
		if(!MCheck.isNull(project_name)){
			if(!url.contains(project_name)){
				url = project_name+url;
			}
		}
		return url;
	}
	public void redirect(String url,String msg){
		url +="?" + msg;
		redirect(url);
	}

	/**
	 * 判断如果是post提交,且有数据,则返回true
	 * 
	 * @return
	 */
	public boolean isPOST() {
		boolean post = false;
		String method = getMethod();
		if(!MCheck.isNull(method)){
			if ("POST".equals(method.toUpperCase())) {
				post = MCheck.isNull(getParaMap()) ? false : true;
			}
		}
		return post;
	}
	public String getPost(String name) {
		String value=null;
		if(isPOST()){
			csrf_verify();
			value= getPara(name);
		}
		return value;
	}
	private void csrf_verify() {
		MToken token = MInit.get().getToken();
		if(token.isProtection()){
			token.csrf_verify();
		}
	}
	@SuppressWarnings("unchecked")
	public <T> T getHead(String name){
		return (T) request.getHeader(name);
	}
	@SuppressWarnings("unchecked")
	public Enumeration  getHeaders(){
		return request.getHeaderNames();
	}
	public String getIpAddress(){
		return request.getRemoteAddr();
	}
	public void setHead(String name,String value){
		response.setHeader(name, value);
	}
	public void setStatus(int status){
		response.setStatus(status);
	}
	public void setDateHead(String name,long date){
		response.setDateHeader(name, date);
	}
	public void setIntHead(String name,int value){
		response.setIntHeader(name, value);
	}
	

}
