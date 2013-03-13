
package com.jzero.db.back;

import com.jzero.util.MRecord;


/** 
 * 2012-10-24: 
 * wangujqw@gmail.com
 */
public interface MBackup {
	 MRecord backup();
	 void load(String filename);
}
