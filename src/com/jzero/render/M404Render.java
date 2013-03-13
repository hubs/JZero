
package com.jzero.render;

import java.io.IOException;
import java.io.PrintWriter;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.util.MCheck;


/** 
 * 2012-10-4:来源JFinal 
 * wangujqw@gmail.com
 */
public class M404Render extends MRender {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String contentType = "text/html;charset=" + getEncoding();
	private static final String defaultHtml = "<html><head><title>404 Not Found</title></head><body bgcolor='white'><center><h1>404 Not Found</h1></center><hr><center>404</center></body></html>";
	private MRender render;
	
	public M404Render(String view) {
		setView(view);
	}
	
	public M404Render() {
		
	}
	
	public void render() {
//		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		
		if (!MCheck.isNull(getView())) {
			render = MInit.get().getMb().getRender(view);
			render.setContext(request, response);
			render.render();
			return;
		}
		
		// render with defaultHtml
		PrintWriter writer = null;
		try {
//			response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
//	        response.setHeader("Cache-Control", "no-cache");
//	        response.setDateHeader("Expires", 0);
			response.setContentType(contentType);
	        writer = response.getWriter();
	        if(writer!=null){
		        writer.write(defaultHtml);
		        writer.flush();
	        }
		} catch (IOException e) {
			Log.me().write_error(e);
		}
		finally {
			if(writer!=null){writer.close();}
		}
	}

	@Override
	public boolean isJsp() {
		// TODO Auto-generated method stub
		return false;
	}
}
