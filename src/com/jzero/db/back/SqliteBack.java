package com.jzero.db.back;

import java.io.File;

import com.jzero.db.core.M;
import com.jzero.util.MDate;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;

public class SqliteBack implements MBackup {

	public MRecord backup() {
		MRecord record = new MRecord();
		String backName = MDate.get_ymd_hms_join() + ".db";
		record.set("name", backName);
		try {
			M.me().execute("backup to "+backName);
			record.set("size", MTool.formart_bytes(new File(backName)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;
	}

	public void load(String filename) {
		M.me().execute("restore from "+filename);
	}

}
