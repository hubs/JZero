
package com.jzero.pool;

import java.sql.Connection;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.log.LogEnum;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/** 2012-10-3 */
public class C3poPool implements IPool {

	public Connection getConnection(String driver, String url, String user,
			String pass) {
		Connection connection=null;
		try {
			ComboPooledDataSource config = new ComboPooledDataSource();
			config.setJdbcUrl(url);
			config.setUser(user);
			config.setPassword(pass);
			config.setDriverClass(driver);
			config.setInitialPoolSize(20);
			config.setMaxPoolSize(100);
			config.setMinPoolSize(10);
			config.setInitialPoolSize(10);
			config.setMaxIdleTime(25000);//最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0
			config.setAcquireIncrement(2);
			config.setIdleConnectionTestPeriod(18000);
			config.setTestConnectionOnCheckin(true);//如果设为true那么在取得连接的同时将校验连接的有效性
			//c3p0将建一张名为Test的空表，并使用其自带的查询语句进行测试。如果定义了这个参数那么属性preferredTestQuery将被忽略。你不能在这张Test表上进行任何操作，它将只供c3p0测试 使用。Default: null
			config.setAutomaticTestTable("c3potest");
			config.setTestConnectionOnCheckout(true);
			config.setPreferredTestQuery("SELECT 1");
			config.setIdleConnectionTestPeriod(18000);//每60秒检查所有连接池中的空闲连接
			connection=config.getConnection();
		} catch (Exception e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
			MInit.get().getMb().getTextRender(e.getMessage()).render();
		}
		return connection;
	}

}
