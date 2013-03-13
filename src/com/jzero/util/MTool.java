package com.jzero.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.jzero.core.MR;
import com.jzero.core.MRouter;
import com.jzero.db.cache.MCache;
import com.jzero.db.cache.MFile;
import com.jzero.log.Log;

public class MTool {

	// --------------------------字符转换区开始----------------------

	public static String UTF8(Object object) {
		String str = null;
		if (!MCheck.isNull(object)) {
			str=object.toString();
			try {
				str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.me().write_error(e);
			}
		}
		return str;
	}

	public static String GBK(Object obj) {
		String str = null;
		if (!MCheck.isNull(obj)) {
			str=obj.toString();
			try {
				str = new String(str.getBytes(), "GB2312");
			} catch (UnsupportedEncodingException e) {
				Log.me().write_error(e);
			}
		}
		return str;
	}

	// --------------------------字符转换区结束----------------------

	

	// --------------------------IP管理区开始----------------------
	private static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; ++i) {
				str = input.readLine();
				if ((str == null) || (str.indexOf("MAC Address") <= 1))
					continue;
				macAddress = str.substring(str.indexOf("MAC Address") + 14, str
						.length());
			}
		} catch (IOException e) {
			return "IOE_ERROR";
		}
		return macAddress;
	}

	public static String getMac() {
		StringBuffer str = new StringBuffer();
		try {
			String ip=getIp();
			InetAddress address = InetAddress.getByName(ip);

			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			if (ni != null) {
				byte[] mac = ni.getHardwareAddress();
				if (mac != null) {
					for (int i = 0; i < mac.length; ++i)
						str.append(String.format("%02X%s", new Object[] {
								Byte.valueOf(mac[i]),
								(i < mac.length - 1) ? "-" : "" }));

				}
				return "IOE_ERROR";
			}
			return getMACAddress(ip);
		} catch (Exception e) {
			Log.me().write_error(e);
		}
		return str.toString();
	}

	public static String getIp() {
	
		String ip = MR.me().getHead("x-forwarded-for");
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	           ip = MR.me().getHead("Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	           ip = MR.me().getHead("WL-Proxy-Client-IP");
	       }
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	           ip = MR.me().getIpAddress();
	       }
	       return ip;
	}

	// --------------------------IP管理区结束----------------------

	// --------------------------字符过滤开始----------------------
	private static String changeCodeForInvite(String con) {
		con = con.replace("\\", "\\\\");
		con = con.replace("'", "\\'");
		con = con.replace("\n", "");
		con = con.replace("\r", "");
		return con;
	}

	private static String removeKey(String str) {
		String inj_str = "'|and|like|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
		String[] inj_stra = inj_str.split("\\|");
		for (int i = 0; i < inj_stra.length; ++i) {
			if (str.indexOf(inj_stra[i]) >= 0) {
				str.replaceAll(inj_stra[i], "..");
			}
		}
		return str;
	}

	private static String removeHtml(String content) {
		if (content != null) {
			content = content.replaceAll("\t", "").replaceAll("\n", " ");
			content = content.replaceAll("<script(.[^(</script>)]*)</script>","[REMOVE]");
//			content = content.replaceAll("<(.[^>]*)>", "[REMOVE]");
		}
		return content;
	}

	public static String removeCharacter(Object str) {
		if(MCheck.isNull(str)){
			return "";
		}
		String newStr = changeCodeForInvite(str.toString());
		newStr = removeHtml(newStr);
		newStr = removeKey(newStr);
		return newStr;
	}

	// --------------------------字符过滤结束----------------------


	// --------------------------HTTP 操作开始
	
	/**
	 * 把'1,2,2,3,5,这样的一条语句分隔到一个数组里面
	 */
	public static String[] lst_to_strs(MRecord re,String index) {
		if (!MCheck.isNull(re)) {
				return re.getStr(index).split("-");
		}
		return null;
	}


	public static String getBase() {
		HttpServletRequest request=MR.me().getRequest();
		return request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + request.getContextPath()+"/";
	}

	/**
	 * 2012-5-16:由于保存时要处理多个表,所以提交上来的Map<String,Object>要删除一些属性,返回一个Map,用于保存数据
	 */
	public static MRecord remove_objects(MRecord r,String... objects) {
		MRecord record = r;
		if (!MCheck.isNull(objects)) {
			record.remove(objects);
		}
		return record;
	}
	/**
	 * 2012-5-16:从传进来的Map<String,Object>中取出指定的字段
	 */
	public static MRecord get_objects(MRecord r,String... objects) {
		MRecord record=new MRecord();
		if (!MCheck.isNull(objects)) {
			for (String obj : objects) {
				record.set(obj, r.getStr(obj));
			}
		}
		return record;
	}
	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		if(!MCheck.isNull(str)){
			Character firstChar = str.charAt(0);
			String tail = str.substring(1);
			str = Character.toLowerCase(firstChar) + tail;
		}
		return str;
	}
	
	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		if(!MCheck.isNull(str)){
			Character firstChar = str.charAt(0);
			String tail = str.substring(1);
			str = Character.toUpperCase(firstChar) + tail;
		}
		return str;
	}
	   //------------------------文件处的内容截取与图片截取开始-----------------/
    //取得页面所有的图片地址
	private static List<String> getImags(String content){
		  String img="";      
		     List<String> pics = new ArrayList<String>();   
		     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址      
		     Pattern   p_image = Pattern.compile(regEx_img,Pattern.CASE_INSENSITIVE);      
		     Matcher m_image = p_image.matcher(content);    
		    while(m_image.find()){      
		         img = img + "," + m_image.group();      
		         Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src   
		         while(m.find()){   
		            pics.add(m.group(1));   
		         }   
		     }      
		   return pics;      
	}
    //取页面中指定的图片
    public static MRecord get_img(MRecord record){
    	if(record.isContainsKey("auto_thumb_no")){
    		int no=record.getInt("auto_thumb_no");//页面中设置的取第几张图片
    		List<String> imgs=getImags(record.getStr("content"));//文本内容
    		int count=imgs.size();
    		if(count>0){
    			if(no>count||no==1){
    				record.set("fimg", imgs.get(0));
    			}else{
    				record.set("fimg", imgs.get(no-1));
    			}
    		}
    		record.remove("auto_thumb_no");
    	}
    	return record;
    }
  
    //取指定长度的简介
    public static MRecord get_intro(MRecord record){
    	if(record.isContainsKey("snippet")){
    		int len=record.getInt("snippet");
    		record.set("intro", record.getStr("content").substring(0,len));
    		record.remove("snippet");
    	}
    	return record;
    }
    //当前路径
    public static String get_path(){
		String directory=MRouter.me().getDirectory();
		String controller= MRouter.me().getController();
		return MCheck.isNull(directory)?controller:(directory.equalsIgnoreCase(controller)?controller:directory+"/"+controller).toLowerCase();
    }
    //判断当前第几层级,是否在子目录下。
    public static int get_level(){
    	String directory=MRouter.me().getDirectory();
		String controller= MRouter.me().getController();
		return MCheck.isNull(directory)?1:directory.equalsIgnoreCase(controller)?1:2;
    }
    //文件备份路径
    public static String back_path(){
    	return MPath.me().getSrcPath()+"/back/";
    }
    //文件保存路径
    public static File import_path(String dir,String filename){
    	String path= MPath.me().getSrcPath()+"/import/"+dir;
    	return MFile.createFile(path, filename);
    }
    //文件基本信息路径
    public static File txt_path(String filename){
    	return import_path("txt",filename);
    }
    //文件下载路径
    public static File down_path(String filename){
    	return import_path("down",filename);
    }
    
    //图片保存路径
    public static File img_path(String filename){
    	return import_path("img",filename);
    }
    
    
    //保存取值的范围不会超过strs长度
    public static  String get(String[] strs,int index){
    	return strs.length > index?strs[index]:null;
    }
    //2012-10-25:解决URI中传递的中文乱码
    public static String decode(String str){
    	try {
			return MCheck.isNull(str)?null:URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.me().write_error(e);
		}
		return "";
    }
    //2013-3-12:进行加密
    public static String encode(String str){
    	try {
			return MCheck.isNull(str)?null:URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.me().write_error(e);
		}
		return null;  	
    }
    
	public static void write(String filename,String content) {
		File file = txt_path(filename);
		BufferedWriter bw =null;
		try{
//			bw = new BufferedWriter(new FileWriter(file)); // 将缓冲对文件的输出
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")); // 将缓冲对文件的输出
			bw.write(content);
			bw.flush();
		}catch(Exception e){
			Log.me().write_error(e);
		}finally{
			try {
				if(!MCheck.isNull(bw)){bw.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String read(String filename) {
		File file = txt_path(filename);
		String output = "";
		if (file.exists()) {
			if (file.isFile()) {
				try {
					
					BufferedReader input = new BufferedReader( new InputStreamReader(new FileInputStream(file), "UTF-8"));
					StringBuffer buffer = new StringBuffer();
					String text;
					while ((text = input.readLine()) != null)
						buffer.append(text);
					output = buffer.toString();
				} catch (IOException e) {
					Log.me().write_error(e);
				}
			}
		}
		return output;
	}
	public static void delete(String filepath){
		MCache.me().delete_file(filepath);
	}
}