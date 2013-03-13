
package com.jzero.pool;

import java.sql.Connection;

/** 2012-10-3 */
public interface IPool {
	Connection getConnection(String driver,String url,String user,String pass);
}
