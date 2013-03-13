package com.jzero.pool;

import java.sql.Connection;
import java.sql.DriverManager;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.log.LogEnum;

/** 2012-10-3 */
public class BasePool implements IPool {

	public Connection getConnection(String driver, String url, String user,
			String pass) {
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
			MInit.get().getMb().getTextRender(e.getMessage()).render();
		}
		return connection;
	}

}
