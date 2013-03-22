package com.jzero.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Properties;
@SuppressWarnings("unchecked")
public class MPro {

	private Properties properties = new Properties();
	private static MPro mpro=new MPro();
	private InputStream inputFile;
	private OutputStream outputStream;
	private String filename = null;

	public static MPro me() {
		return mpro;
	}
	public void printProperties(){
		properties.list(System.out);
	}

	public MPro load_file(String fileName) {
		loadPro(fileName);
		this.filename = fileName;
		return this;
	}

	private void loadPro(String fileName) {
		try {
			inputFile = getInputStream(fileName);
			if(!MCheck.isNull(inputFile)){
				properties.load(inputFile);
				
				if(getBool("show_prop")){
					MPrint.print(fileName);
					properties.list(System.out);
				}
				inputFile.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
//			Log.me().write_log(LogEnum.ERROR, ex.getMessage());
		}
	}
	private InputStream getInputStream(String fileName) {
		String path=null;
		try {
			path = MPath.me().getRootClassPath() + "/" + fileName;
			return new FileInputStream(path);
		} catch (IOException e) {
			e.printStackTrace();
			MPrint.print("加载文件失败"+path);
//			Log.me().write_error(e);
		}
		return null;
	}

	private OutputStream getOutputStream(String fileName) throws Exception {
		return new FileOutputStream(new File(getfilePath(fileName)));
	}
	private String getfilePath(String filename){
		return MPath.me().getWebInfPath()+"classes/"+filename;
	}

	public Object getObj(String k) {
		return properties == null ? null : properties.get(k);
	}

	public String getStr(String k) {
		return MCheck.isNull(getObj(k)) ? "" : getObj(k).toString();
	}

	public boolean getBool(String k) {
		return MCheck.isNull(getStr(k)) ? false : Boolean.valueOf(getStr(k));
	}

	public int getInt(String k) {
		return MCheck.isNull(getStr(k)) ? 0 : Integer.parseInt(getStr(k));
	}

	public void setValue(String name, String value) {
		if (MCheck.isNull(properties)) {
			MPrint.print("pri is null!!");
		} else {
			try {
				outputStream = getOutputStream(filename);
				outputStream = getOutputStream(filename);
				properties.setProperty(name, value);
				properties.store(outputStream,"JZero");
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保存到指定的文件中,test 
	 */
	public void setValue_file(String filename,String name, String value) {
		if (MCheck.isNull(properties)) {
			MPrint.print("pri is null!!");
		} else {
			try {
				outputStream = getOutputStream(filename);
				outputStream = getOutputStream(filename);
				properties.setProperty(name, value);
				properties.store(outputStream,"JZero");
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings( { "deprecation"})
	public static String getPath(String packName, Class cls) {
		String currentPath = cls.getResource("/" + packName).toString();
		currentPath = currentPath.substring(6);
		return URLDecoder.decode(currentPath);
	}
}
