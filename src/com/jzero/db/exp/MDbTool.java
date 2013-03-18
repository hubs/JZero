
package com.jzero.db.exp;

import com.jzero.util.MCheck;
import com.jzero.util.MEncrypt;
import com.jzero.util.MPath;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;

/** 2012-10-3 */
public class MDbTool {
	// -------------------------读取当前数据库开始
	// 2012-4-12:得到当前所使用的数据库
	public static String getWhoDb() {
		return MPro.me().getStr("dbdriver");
	}

	// 得到连接数据库连接
	public static MRecord getDbConnectInfo() {
		// 读取配置文件
		MRecord record = new MRecord();
		// 得到是哪个数据库
		String db = getWhoDb();
		String user = MEncrypt.decrypt(MPro.me().getStr("username"));
		String _temp_pass=MPro.me().getStr("password");
		String pwd =MCheck.isNull(_temp_pass)?"": MEncrypt.decrypt(_temp_pass);
		String port =MPro.me().getStr("port");
		String addr =MPro.me().getStr("hostname");
		String dbname =MPro.me().getStr("database");
		String url = null, driver = null;
		if ("oracle".equalsIgnoreCase(db)) {
			url = "jdbc:oracle:thin:@" + addr + ":" + port + ":" + dbname;
			driver = "oracle.jdbc.driver.OracleDriver";
		} else if ("mssql".equalsIgnoreCase(db)) {
			url = "jdbc:jtds:sqlserver://" + addr + ":" + port + "/" + dbname;
			driver = "net.sourceforge.jtds.jdbc.Driver";
		} else if ("mysql".equalsIgnoreCase(db)) {
			url="jdbc:mysql://"+addr+":"+port+"/"+dbname+"?useUnicode=true&amp;characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false";
			driver = "com.mysql.jdbc.Driver";
		}else if("sqlite".equalsIgnoreCase(db)){
			String mysqlpaths=MPath.me().getWebInfPath()+"classes/";
			url="jdbc:sqlite:"+mysqlpaths+dbname;//sqlite是嵌入式,所以不用指定addr,port
			driver = "org.sqlite.JDBC";
		}
		record.set("url", url);
		record.set("driver", driver);
		record.set("user", user);
		record.set("pass", pwd);
		return record;
	}

	// -------------------------读取当前数据库结束
}
