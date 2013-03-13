package com.jzero.db.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jzero.json.JSONObj;
import com.jzero.log.Log;
import com.jzero.log.LogEnum;
import com.jzero.util.MCheck;
import com.jzero.util.MPrint;
import com.jzero.util.MRecord;

/**
 * 2012-10-3: wangujqw@gmail.com
 */
public class MFile {
	private static Map<File, StringBuffer> content = new HashMap<File, StringBuffer>();

	public static BufferedWriter getWriter(File file_name, boolean isAppend) {
		try {
			return file_name.exists() ? new BufferedWriter(new FileWriter(
					file_name, isAppend)) : null;
		} catch (IOException e) {
			Log.me().write_error(e);
		}
		return null;
	}

	public static BufferedReader getReader(File file_name) {
		try {
			return new BufferedReader(new FileReader(file_name));
		} catch (FileNotFoundException e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
		}
		return null;
	}

	public static void write(File file_name, List<MRecord> lst) {
		BufferedWriter w = getWriter(file_name, false);
		try {
			if (!MCheck.isNull(w)) {
				w.write(JSONObj.toJSONString(lst));
			}
		} catch (IOException e) {
			Log.me().write_error(e);
		} finally {
			try {
				if (!MCheck.isNull(w)) {
					w.flush();
					w.close();
				}
			} catch (IOException e) {
				Log.me().write_error(e);
			}
		}
	}

	private static String getStr(File file_name) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = getReader(file_name);
		try {
			// char c[]=new char[1024];fsdf
			if (!MCheck.isNull(br)) {
				String line = br.readLine();
				while (!MCheck.isNull(line)) {
					sb.append(line.trim());
					line = br.readLine();
				}
				content.put(file_name, sb);
			}
		} catch (IOException e) {
			Log.me().write_error(e);
		} finally {
			try {
				if (!MCheck.isNull(br)) {
					br.close();
				}
			} catch (IOException e) {
				Log.me().write_error(e);
			}
		}
		return sb.toString();
	}

	public static List<MRecord> read(File file_name) {
		return MCheck.isNull(file_name) ? null : JSONObj
				.parseList(getStr(file_name));
	}

	// 创建目录
	public static File createDir(String dir_name) {
		File file = new File(dir_name);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	// 创建文件
	public static File createFile(String dir_name, String file_name) {
		File file = createDir(dir_name);
		file = new File(file, file_name);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				MPrint.print(e.getMessage());
			}
		}
		return file;
	}

}
