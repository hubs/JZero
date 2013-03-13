
package com.jzero.render;

import java.io.IOException;
import java.io.PrintWriter;

import com.jzero.log.Log;

/** 
 * 2012-10-3: 来源于JFinal
 * wangujqw@gmail.com
 */
public class MJsonRender extends MRender {

	private static final long serialVersionUID = 1L;

	/**
	 * http://zh.wikipedia.org/zh/MIME
	 * 在wiki中查到: 尚未被接受为正式数据类型的subtype，可以使用x-开始的独立名称（例如application/x-gzip）
	 * 所以以下可能要改成 application/x-json
	 * 
	 * 通过使用firefox测试,struts2-json-plugin返回的是 application/json, 所以暂不改为 application/x-json
	 * 1: 官方的 MIME type为application/json, 见 http://en.wikipedia.org/wiki/MIME_type
	 * 2: ie 不支持 application/json, 在 ajax 上传文件完成后返回 json时 ie 提示下载文件
	 */
	private static final String contentType = "application/json;charset=" + getEncoding();
	
	private String value;
	
	public MJsonRender(String value) {
		this.value = value;
	}
	
	public void render() {
		
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType(contentType);
			writer = response.getWriter();
	        writer.write(value);
	        writer.flush();
		} catch (IOException e) {
			Log.me().write_error(e);
		}
		finally {
			writer.close();
		}
	}

	@Override
	public boolean isJsp() {
		// TODO Auto-generated method stub
		return false;
	}
}
