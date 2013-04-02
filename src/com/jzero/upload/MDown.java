package com.jzero.upload;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.jzero.util.MCheck;

public class MDown {

	/**
	 * 下载远程文件
	 * 
	 * @param photoUrl
	 *            文件路径
	 * @param fileName
	 *            下载到本地的路径
	 * @return 是否成功
	 * @throws Exception 
	 */
	public static boolean saveUrlAs(String photoUrl, String fileName) throws Exception {
			URL url = new URL(photoUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			DataOutputStream out =null;
			DataInputStream in =null;
			boolean isOk=false;
			try{
				out = new DataOutputStream(new FileOutputStream(fileName));
				if(!MCheck.isNull(connection)){
					in = new DataInputStream(connection.getInputStream());
					byte[] buffer = new byte[4096];
					int count = 0;
					while ((count = in.read(buffer)) > 0) {
						out.write(buffer, 0, count);
					}
					isOk=true;
				}
			}finally{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			return isOk;
	}

	/**
	 * 获取远程文件的内容（兼容HTTP与FTP）
	 * 
	 * @param urlString
	 *            文件路径
	 * @return 文件内容
	 * @throws Exception 
	 */
	public static String getDocumentAt(String urlString) throws Exception {
		StringBuffer document = new StringBuffer();
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		BufferedReader reader =null;
		try{
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				document.append(line + "\n");
			}
		}finally{
			if(reader!=null){
				reader.close();
			}
		}
		return document.toString();
	}

	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String photoUrl = "http://192.168.1.110:8090/ddsx/g3resource/video/c74e5e9cca2847b7578c11d2674c8d55.flv";
		String fileName = photoUrl.substring(photoUrl.lastIndexOf("/"));
		String filePath = "c:/";
		System.out.println(getDocumentAt(photoUrl));
		boolean flag = saveUrlAs(photoUrl, filePath + fileName);
		System.out.println("Run ok!\n Get URL file " + flag);
	}
}
