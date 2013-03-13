
package com.jzero.render;

import java.io.IOException;
import java.io.PrintWriter;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.util.MCheck;

/** 
 * 2012-10-4: 
 * wangujqw@gmail.com
 */
public class M500Render extends MRender {
	private static final long serialVersionUID = 1L;
	private static final String contentType = "text/html;charset=" + getEncoding();
	private static final String defaultHtml = "<html><head><title>500 Internal Server Error</title></head><body bgcolor='white'><center><h1>500 Internal Server Error</h1></center><hr><center>500 ERROR</center></body></html>";
	private MRender render;
	
	public M500Render(String view) {
		this.view = view;
	}
	
	public M500Render() {
		
	}
	
	public void render() {
//		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		// render with view
		if (!MCheck.isNull(getView())) {
			render = MInit.get().getMb().getRender(view);
			render.setContext(request, response);
			render.render();
			return;
		}
		
		// render with defaultHtml
		PrintWriter writer = null;
		try {
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
			if(!MCheck.isNull(writer)){
				writer.close();
			}
			
		}
	}

	@Override
	public boolean isJsp() {
		// TODO Auto-generated method stub
		return false;
	}
}
