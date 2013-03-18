package com.jzero.db.back;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.jzero.util.MCheck;
import com.jzero.util.MDate;
import com.jzero.util.MEncrypt;
import com.jzero.util.MPath;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;

/**
 * 2012-10-24: wangujqw@gmail.com
 */
public class MysqlBack implements MBackup {

	public MRecord backup() {
		MRecord record = new MRecord();
		MPro pros = MPro.me();
		String user = MEncrypt.decrypt(MPro.me().getStr("username"));
		String _temp_pass = MPro.me().getStr("password");
		String pwd = MCheck.isNull(_temp_pass) ? "" : MEncrypt.decrypt(_temp_pass);
		String hostname = pros.getStr("hostname");
		String database = pros.getStr("database");
		String back_file = MPath.me().getSrcPath() + "/back/";
//		String mysqlpaths = pros.getStr("mysql_path");
		String mysqlpaths=MPath.me().getWebInfPath()+"classes/";
		String backName = MDate.get_ymd_hms_join() + ".sql";
		record.set("name", backName);
		File backupath = new File(back_file);
		if (!backupath.exists()) {
			backupath.mkdir();
		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(mysqlpaths).append("mysqldump.exe -h ").append(hostname)
					.append(" -u").append(user).append("  -p").append(pwd)
					.append(" ").append(database);
			Process p = Runtime.getRuntime().exec(sb.toString());
			copy(p, new File(back_file, backName));
			record.set("size", MTool.formart_bytes(new File(back_file, backName)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;

	}

	

	private void copy(Process p, File dst) {
		// 设置导出编码为utf-8。这里必须是utf-8
		// 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
		BufferedReader br = null;
		OutputStreamWriter writer = null;
		try {
			// 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
			String inStr;
			StringBuffer sb = new StringBuffer("");
			// 组合控制台输出信息字符串
			br = new BufferedReader(new InputStreamReader(p.getInputStream(),
					"utf-8"));
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			// 要用来做导入用的sql目标文件：
			writer = new OutputStreamWriter(new FileOutputStream(dst), "utf-8");
			writer.write(sb.toString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(!MCheck.isNull(br)){br.close();}
				if(!MCheck.isNull(br)){writer.close();}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void load(String filename) {
		String mysqlpaths = MPro.me().getStr("mysql_path");
		String user = MEncrypt.decrypt(MPro.me().getStr("username"));
		String _temp_pass = MPro.me().getStr("password");
		String pwd = MCheck.isNull(_temp_pass) ? "" : MEncrypt.decrypt(_temp_pass);
		String back_file = MPath.me().getSrcPath() + "/back";
		String filepath = back_file + filename; // 备份的路径地址

		// 新建数据库finacing
		String stmt1 = mysqlpaths + " mysqladmin -u " + user + " -p" + pwd+ " create finacing"; // -p后面加的是你的密码
		String stmt2 = "mysql -u " + user + " -p" + pwd + " finacing < "+ filepath;
		String[] cmd = { "cmd", "/c", stmt2 };
		try {
			Runtime.getRuntime().exec(stmt1);
			Runtime.getRuntime().exec(cmd);
			System.out.println("数据已从 " + filepath + " 导入到数据库中");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
