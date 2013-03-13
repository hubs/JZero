package com.jzero.core;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jzero.render.MB;
import com.jzero.token.MToken;
import com.jzero.util.MPro;
import com.jzero.util.Msg;

public class MInit{
	private static MPro config = null;
	private MR mr = null;
	private MRouter router = null;
	private MURI uri = null;
	private MToken token = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private MB mb=null;

	// ---------------------------------------
	private final static ThreadLocal<MInit> contexts = new ThreadLocal<MInit>();
	static{
		config = MPro.me().load_file(Msg.CONFIG).load_file(Msg.DB_CONFIG).load_file(Msg.DB_CONFIG).load_file(Msg.ROUTE_CONFIG).load_file(Msg.OTHER_CONFIG);
	}
	public static MInit begin(HttpServletRequest req, HttpServletResponse res) {
		MInit init = new MInit();
		init.request = req;
		init.response = res;
		init.response.setCharacterEncoding("UTF-8");
		init.session = req.getSession(false);
		init.cookies=req.getCookies();
		init.mr = MR.init(init);
		init.token = MToken.init(init);
		init.uri =  MURI.init(init);
		init.router = MRouter.init(init);
		init.mb=new MB();
		contexts.set(init);
		return init;
	}
	public static MInit get(){
		return contexts.get();
	}
	public  void end(){
		this.request=null;
		this.response=null;
		this.session=null;
		this.cookies=null;
		
		this.mr=null;
		this.token=null;
		this.uri=null;
		this.router=null;
		this.mb=null;
		contexts.remove();
	}

	public MB getMb() {
		return mb;
	}

	private HttpSession session;
	private Cookie[] cookies;
	
	public HttpSession getSession() {
		return session;
	}
	public Cookie[] getCookie(){
		return cookies;
	}
	public String uri(){
		return request.getRequestURI();
	}

	// -------------------------------------------------

	public MToken getToken() {
		return token;
	}

	public MR getMr() {
		return mr;
	}

	public MRouter getRouter() {
		return router;
	}

	public MURI getURI() {
		return uri;
	}

	public MPro getConfig() {
		return config;
	}

	public HttpServletRequest request() {
		return request;
	}

	public HttpServletResponse response() {
		return response;
	}

}
