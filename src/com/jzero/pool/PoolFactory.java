package com.jzero.pool;

import java.sql.Connection;

import com.jzero.db.exp.MDbTool;
import com.jzero.log.Log;
import com.jzero.log.LogEnum;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;

/** 2012-10-3 */
public final class PoolFactory {

	public static Connection getConnection() {
		MRecord record = MDbTool.getDbConnectInfo();
		String pool_name="com.jzero.pool."+MPro.me().getStr("pool");
		IPool pool = null;
		try {
			pool = (IPool) Class.forName(pool_name).newInstance();
		} catch (Exception e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
		}
		String driver = record.getStr("driver");
		String url = record.getStr("url");
		String user = record.getStr("user");
		String pass = record.getStr("pass");
		return pool.getConnection(driver, url, user, pass);
	}

}
