
package com.jzero.render;

import java.io.IOException;
import java.io.PrintWriter;

import com.jzero.log.Log;
import com.jzero.util.MCheck;

/** 
 * 2012-10-3: 来源于JFinal
 * wangujqw@gmail.com
 */
class MTextRender extends MRender {
	
	private static final long serialVersionUID = -6559692075690567088L;

	private static final String defaultContentType = "text/plain;charset=" + getEncoding();
	
	private String text;
	
	public MTextRender(String text) {
		this.text = text;
	}
	
	private String contentType;
	public MTextRender(String text, String contentType) {
		this.text = text;
		this.contentType = contentType;
	}
	
	public void render() {
		PrintWriter writer = null;
		try {
			if(!MCheck.isNull(response)){
				response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
		        response.setHeader("Cache-Control", "no-cache");
		        response.setDateHeader("Expires", 0);
		        
		        if (contentType == null) {
		        	response.setContentType(defaultContentType);
		        }
		        else {
		        	response.setContentType(contentType);
					response.setCharacterEncoding(getEncoding());
		        }
		        
		        writer = response.getWriter();
		        if(!MCheck.isNull(writer)){
			        writer.write(text);
			        writer.flush();
		        }
			}
		} catch (IOException e) {
			Log.me().write_error(e);
		}
		finally {
			  if(!MCheck.isNull(writer)){writer.close();}
		}
	}

	@Override
	public boolean isJsp() {
		// TODO Auto-generated method stub
		return false;
	}
}




