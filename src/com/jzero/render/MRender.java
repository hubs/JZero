package com.jzero.render;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2012-10-3: 主要用于页面的渲染,来源于JFinal wangujqw@gmail.com
 */
public abstract class MRender implements Serializable {

	private static final long serialVersionUID = 1617632731853793227L;
	protected String view=null;
	protected transient HttpServletRequest request;
	protected transient HttpServletResponse response;

	final static String getEncoding() {
		return "UTF-8";
	}

	public final MRender setContext(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
		return this;
	}

	public final MRender setContext(HttpServletRequest request,
			HttpServletResponse response, String viewPath) {
		this.request = request;
		this.response = response;
		if (viewPath != null && !viewPath.startsWith("/"))
			viewPath ="/"+ viewPath;
		setView(viewPath);
		return this;
	}

	/**
	 * Render to client
	 */
	public abstract void render();
	public abstract boolean isJsp();

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
}