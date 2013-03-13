
package com.jzero.render;

import javax.servlet.http.HttpServletResponse;

/** 
 * 2012-10-3: 来源于JFinal
 * wangujqw@gmail.com
 */
class M301Render extends MRender {
	
	private static final long serialVersionUID = 6487819781831800339L;
	private String url;
	private boolean withOutQueryString;
	
	public M301Render(String url) {
		this.url = url;
		this.withOutQueryString = false;
	}
	
	public M301Render(String url, boolean withOutQueryString) {
		this.url = url;
		this.withOutQueryString = withOutQueryString;
	}
	
	public void render() {
		if (withOutQueryString == false) {
			String queryString = request.getQueryString();
			queryString = (queryString == null ? "" : "?" + queryString);
			url = url + queryString;
		}
		
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.setHeader("Location", url);
		response.setHeader("Connection", "close");
	}

	@Override
	public boolean isJsp() {
		return false;
	}
}
