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
		return new FileOutputStream(new File(getPath(fileName, getClass())));
	}

	private OutputStream getOutputStreamSrc(String fileName) throws Exception {
		return new FileOutputStream(new File(MPath.me().getSrcPath() + "\\"+ fileName));
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
			properties.setProperty(name, value);
			try {
				// save classpath目录　
				outputStream = getOutputStream(filename);
				properties.store(outputStream, name + "=" + value);
				outputStream.flush();

				// save src目录下的文件
				outputStream = getOutputStreamSrc(filename);
				properties.store(outputStream, name + "=" + value);
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
//				Log.me().write_log(LogEnum.ERROR, e.getMessage());
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
