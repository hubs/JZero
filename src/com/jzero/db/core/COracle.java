package com.jzero.db.core;

import java.util.List;

import com.jzero.util.MRecord;

public class COracle extends CBase {

	@Override
	public List<MRecord> pager(String table, String where, String field,
			int current, int pageSize, Object... params) {
		return null;
	}

	@Override
	public List<MRecord> pager_sql(String sql, int current, int pageSize,
			Object... params) {
		return null;
	}

	@Override
	public MRecord select_one(String table, String where, String field,
			boolean cache, Object... params) {
		return null;
	}

}
