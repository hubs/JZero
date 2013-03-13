package com.jzero.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
@SuppressWarnings("unchecked")
public class MPath {
	private MPath() {
	}

	private static MPath path = new MPath();

	public static MPath me() {
		return path;
	}

	public String getWebClassesPath() {
		return getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	/**
	 * App Run: /F:/demo/wzybcx/WebRoot/WEB-INF/lib/myjar.jar
	 * 
	 * 页面加载 /D:/Program Files/Apache Software Foundation/Tomcat
	 * 6.0/webapps/wzybcx/WEB-INF/lib/myjar.jar
	 * 
	 * @return
	 */
	public String getSrcPath() {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF"));
		}
		path = new File(path).getAbsolutePath();
		return path;
	}

	public String getWebInfPath() {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF") + 8);
		}
		return path;
	}

	public String getWebRoot() {
		String path = getWebClassesPath();
//		MPrint.print(MPath.me().getWebClassesPath());///D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/JZero/WEB-INF/classes/com/jzero/util/MPath.class
//		MPrint.print(MPath.me().getWebInfPath());///D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/JZero/WEB-INF/
//		MPrint.print(MPath.me().getPath(MPath.class));//D:\Program%20Files\Apache%20Software%20Foundation\Tomcat%206.0\webapps\JZero\WEB-INF\classes\com\jzero\log
//		MPrint.print(MPath.me().getRootClassPath());//->D:\Program%20Files\Apache%20Software%20Foundation\Tomcat%206.0\webapps\JZero\WEB-INF\classes
//		MPrint.print(MPath.me().getSrcPath());
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF"));
		}
		return path;
	}

	private static String webRootPath;


	
	public  String getPath(Class clazz) {
		String path = clazz.getResource("/").getPath();
		return new File(path).getAbsolutePath().replaceAll("%20", " ");
	}

	public  String getPath(Object object) {
		String path = object.getClass().getResource("").getPath();
		return new File(path).getAbsolutePath();
	}

	public  String getRootClassPath() {
		String path = MPath.class.getClassLoader().getResource("").getPath();
		 path= new File(path).getAbsolutePath();
		return path.replaceAll("%20", " ");
	}

	public  String getPackagePath(Object object) {
		Package p = object.getClass().getPackage();
		return p != null ? p.getName().replaceAll("\\.", "/") : "";
	}

	public  Properties getFileFromJar(String file) throws Exception {
	     String dirPath = file; //jar中的目录名称
         URL url = this.getClass().getClassLoader().getResource(dirPath);//加载
         String urlStr = url.toString();
         String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2); //将jar文件读取出来
         URL jarURL = new URL(jarPath); 
         JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection(); //对jar文件进行加载
         JarFile jarFile = jarCon.getJarFile(); 
         Enumeration<JarEntry> jarEntrys = jarFile.entries(); //得到该jar文件下的所有目录文件
         Properties props = new Properties(); 
         while (jarEntrys.hasMoreElements()) { 
             JarEntry entry = jarEntrys.nextElement(); 
             // 简单的判断路径，如果想做到想Spring，Ant-Style格式的路径匹配需要用到正则。 
            String name = entry.getName(); 
            MPrint.print("scan "+name);
            if (name.startsWith(dirPath) && !entry.isDirectory()) { //如果名称与dirpath相等,说明找到了当前文件,则进行加载 开始读取文件内容 
                 InputStream is = this.getClass().getClassLoader().getResourceAsStream(name); 
                 props.load(is); 
            } 
         }
		return props; 
	}

	public  String getWebRootPath() {
		if (webRootPath == null)
			webRootPath = detectWebRootPath();
		return webRootPath;
	}

	public  void setWebRootPath(String webRootPath) {
		if (webRootPath.endsWith(File.separator))
			webRootPath = webRootPath.substring(0, webRootPath.length() - 1);
		MPath.webRootPath = webRootPath;
	}

	private  String detectWebRootPath() {
		try {
			String path = MPath.class.getResource("/").getFile();
			return new File(path).getParentFile().getParentFile()
					.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) throws Exception {
		MPath.me().setWebRootPath("/");
		MPrint.print(MPath.me().getWebRoot());///D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/JZero/
		MPrint.print(MPath.me().getWebClassesPath());///D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/JZero/WEB-INF/classes/com/jzero/util/MPath.class
		MPrint.print(MPath.me().getWebInfPath());///D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/JZero/WEB-INF/
		MPrint.print(MPath.me().getPath(MPath.class));//D:\Program%20Files\Apache%20Software%20Foundation\Tomcat%206.0\webapps\JZero\WEB-INF\classes\com\jzero\log
		MPrint.print(MPath.me().getRootClassPath());//->D:\Program%20Files\Apache%20Software%20Foundation\Tomcat%206.0\webapps\JZero\WEB-INF\classes
	}
}
