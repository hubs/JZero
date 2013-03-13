package com.jzero.core;

import java.util.regex.Pattern;

import com.jzero.util.MCheck;
import com.jzero.util.MPrint;
import com.jzero.util.MTool;

/**
 * 2012-10-3: 参照CI URI ,解析URI
 * wangujqw@gmail.com
 */
public class MURI {
	private String SLASH = "/";

	// 返回的参数
	private String[] uri_strs;
	
	private  Pattern p = null;
	public static MURI me() {
		return MInit.get().getURI();
	}
	public static MURI init(MInit init){
		MURI uri=new MURI();
		uri.p=Pattern.compile(init.getConfig().getStr("permitted_uri_chars"));
		return uri;
	}

	public String seg_str(int index) {
		return MCheck.isNull(getUri_strs()) ? null : getUri_strs().length > index ? MTool.UTF8(index_uri(index)) :null ;
	}

	public String seg_str(int index, String defaultValue) {
		return MCheck.isNull(getUri_strs()) ? defaultValue: getUri_strs().length>index?MTool.UTF8(index_uri(index)):null;
	}
	public String seg_str(int index,String[] uris){
		return MCheck.isNull(uris)?null:uris.length>index?index_uri(index,uris):null;
	}
	public int seg_int(int index) {
		return MCheck.isNull(getUri_strs()) ? null :getUri_strs().length>index?Integer.parseInt(index_uri(index)):null ;
	}
	public int seg_int(int index,String[] uris){
		return MCheck.isNull(uris)?null:getUri_strs().length>index?Integer.parseInt(index_uri(index,uris)):null;
	}

	public int seg_int(int index, int defaultValue) {
		return MCheck.isNull(getUri_strs()) ? defaultValue : getUri_strs().length>index?Integer.parseInt(index_uri(index)):null;
	}
	
	private String index_uri(int index){
		String value=getUri_strs()[index];
		if(!p.matcher(value).matches()){
			MInit.get().getMb().getTextRender("The URI you submitted has disallowed characters.=>"+value);
		}
		return value;
	}
	private String index_uri(int index,String[] uris){
		String value=uris[index];
		if(!p.matcher(value).matches()){
			MInit.get().getMb().getTextRender("The URI you submitted has disallowed characters.");
		}
		return value;
	}
	/**
	 * http://localhost:8080/JZero/News/index/t1/t2/t3
	 * /JZero/News/index/t1/t2/t3 在过滤器中可以传递值过来
	 * @param uri 
	 */
	public String[] parse(String uri) {
//		setUri_string(uri);
		if (!MCheck.isNull(uri)) {
			if (uri.contains("http")) {
				uri_strs = parseURL(uri);
			} else {
				uri_strs = parseURI(uri);
			}
		}
		return uri_strs;
	}


	// ->/JZero/News(0)/index(1)/t1(2)/t2(3)/t3(4)
	private String[] parseURI(String uri) {
		String uri_strs[]=null;
		if(!MCheck.isNull(uri)){
			String proName=MInit.get().getMr().getProject();
			if(!MCheck.isNull(proName)){
				uri_strs = uri.replace(proName, "").replaceFirst("/", "").split(SLASH);
			}else{
				uri_strs=uri.replaceFirst("/", "").split(SLASH);
			}
		}
		return uri_strs;
	}
	public static void main(String[] args) {
		String string="/index/download";
		if(!MCheck.isNull(string)){
				String[] uri_strs=string.replaceFirst("/", "").split("/");
				for(String s:uri_strs){
					MPrint.print(s);
				}
		}
	}

	// ->http://localhost:8080/JZero/News/index/t1/t2/t3
	private String[] parseURL(String uri) {
		String uri_strs[]=null;
		if(!MCheck.isNull(uri)){
			String proName=MInit.get().getMr().getProject();
			if(!MCheck.isNull(proName)){
				uri_strs = uri.replace(proName, "").replaceFirst("/", "").split(SLASH);
			}else{
				uri_strs=uri.replaceFirst("/", "").split(SLASH);
			}
		}
		return uri_strs;
	}

//	public String getUri_string() {
//		return uri_string;
//	}

	public String[] getUri_strs() {
		return uri_strs;
	}

	public void setUri_strs(String[] uriStrs) {
		uri_strs = uriStrs;
	}
	
}
