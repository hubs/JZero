package com.jzero.pool;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.log.LogEnum;

/** 2012-10-3 */
public class BoneCpPool implements IPool {

	public Connection getConnection(String driver,String url,String user,String pass) {
		Connection connection=null;
		try {
			Class.forName(driver);
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(url);
			config.setUsername(user);
			config.setPassword(pass);
			config.setMinConnectionsPerPartition(10); //设置每个分区中的最小连接数 10
			config.setMaxConnectionsPerPartition(30);//设置每个分区中的最大连接数 30
			config.setPartitionCount(3);//设置分区  分区数为3
			config.setStatisticsEnabled(true);
            config.setAcquireIncrement(5);//当连接池中的连接耗尽的时候 BoneCP一次同时获取的连接数
            config.setReleaseHelperThreads(3);//连接释放处理
            config.setIdleConnectionTestPeriod(5, TimeUnit.MINUTES);//设置每60秒检查数据库中的空闲连接数
            config.setIdleMaxAge(5, TimeUnit.MINUTES);    //设置连接空闲时间
			BoneCP connectionPool = new BoneCP(config);
			connection=connectionPool.getConnection();
		} catch (Exception e) {
			Log.me().write_log(LogEnum.ERROR, e.getMessage());
			MInit.get().getMb().getTextRender(e.getMessage()).render();
		}
		return connection;
	}
}
