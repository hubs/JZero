
package com.jzero.pool;

import java.sql.Connection;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.log.LogEnum;

/** 2012-10-3 */
public class ProxoolPool implements IPool {

	public Connection getConnection(String driver, String url, String user,
			String pass) {
		Connection connection=null;
		try {
			ProxoolDataSource config=new ProxoolDataSource();
			config.setDriverUrl(url);
			config.setUser(user);
			config.setPassword(pass);
			config.setHouseKeepingSleepTime(9000);
			config.setMaximumConnectionCount(100);
			config.setMinimumConnectionCount(10);
			config.setMaximumActiveTime(20);
			config.setTestBeforeUse(true);
			config.setHouseKeepingTestSql("select 1");
			config.setDriver(driver);
			connection=config.getConnection();
		} catch (Exception e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
			MInit.get().getMb().getTextRender(e.getMessage()).render();
		}
		return connection;
	}

}
