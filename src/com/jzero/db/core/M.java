package com.jzero.db.core;

import java.sql.Connection;
import java.util.List;

import com.jzero.cache.C;
import com.jzero.comm.MCommHelp;
import com.jzero.core.MR;
import com.jzero.core.MURI;
import com.jzero.db.core.CBase.ITX;
import com.jzero.json.JSONObj;
import com.jzero.log.Log;
import com.jzero.util.MCheck;
import com.jzero.util.MDividPage;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;
import com.jzero.util.Msg;

public class M {

	private M() {
	}

	private static M dao = new M();

	private static CBase db = null;

	public static M me() {
		if (MCheck.isNull(db)) {
			db = init();
		}
		return dao;
	}

	public Connection getConn() {
		return db.getConnection();
	}

	private static CBase init() {
		CBase db = null;
		try {
			String sqlType = "com.jzero.db.core." + MPro.me().getStr("db_type");
			db = (CBase) Class.forName(sqlType).newInstance();
		} catch (Exception e1) {
			Log.me().write_error(e1);
		}
		
		return db;
	}

	public void batch_insert(String sql, Object[][] objs) {
		db.insert_batch(sql, objs);
	}

	// 插入后返回ID
	public int insert_reid(String table, MRecord inDatas) {
		db.insert(table, inDatas);
		List<MRecord> lst = get_field("max(id) id ", table);
		return MCheck.isNull(lst) ? 0 : lst.get(0).getInt("id");
	}

	public int insert(String table, MRecord inDatas) {
		return db.insert(table, inDatas);
	}

	public int update(String table, MRecord inDatas, String where) {
		return db.update(table, inDatas, where);
	}

	public int delete(String table, String where) {
		return db.delete(table, where);
	}

	public MRecord one_t(String table, String where, Object... params) {
		return db.one_t(table, where, "*", true, params);
	}

	public MRecord one_t(String table, Object[] order) {
		return db.one_t(table, null, null, true, order);
	}

	public MRecord one_t_n(String table, String where, Object... params) {
		return db.one_t(table, where, "*", false, params);
	}

	public MRecord one_s(String sql) {
		return db.one_s(sql);
	}

	public boolean isExist(String table, String where, Object... params) {
		MRecord r = one_t(table, where, params);
		return MCheck.isNull(r) ? false : true;
	}

	public List<MRecord> sql(String sql, Object... params) {
		return db.select(sql, params);
	}

	public void execute(String sql, Object... params) {
		execute(getConn(), sql, params);
	}

	public void execute(Connection conn, String sql, Object... params) {
		int reInt = db.update(conn, sql, params);
		if (reInt > 0) {
			Log.me().write_info("sql:" + sql);
		}
	}

	public List<MRecord> get_field(String field, String table,
			Object... orderStr) {
		return db.select(table, "", field, orderStr);
	}

	public List<MRecord> get_data(String table, Object... orderStr) {
		return db.select(table, "", "", orderStr);
	}

	public List<MRecord> get_where(String table, String where,
			Object... orderStr) {
		return db.select(table, where, "", orderStr);
	}

	public List<MRecord> get_data(String field, String table, String where,
			Object... orderStr) {
		return db.select(table, where, field, orderStr);
	}

	public List<MRecord> get_table(String table, Object... order) {
		return db.select(table, null, null, order);
	}

	// ----------------------分页操作开始
	private static int getCurrenPage(Integer seg) {
		int level = MTool.get_level();
		int seg_page = MCheck.isNull(seg) ? level == 1 ? 4 : 5 : seg;
		String showpage = MURI.me().seg_str(seg_page);
		return MCheck.isNull(showpage) ? 1 : Integer.parseInt(showpage);
	}

	public void get_pager(String table, String where, Integer pageSize,
			Integer page_seg, Object... order) {
		List<MRecord> lst= get_pager_p(table, where, pageSize, page_seg, null, order);
		MR.me().setAttr(Msg.LIST_DATAS, lst);
	}

	public void get_pager(String table, String where, Integer page_seg,
			Object... order) {
		List<MRecord> lst = get_pager_p(table, where, null, page_seg, null,
				order);
		MR.me().setAttr(Msg.LIST_DATAS, lst);
	}

	public List<MRecord> get_pager_c(String table, String where,
			String cacheKey, Object... order) {
		return get_pager_p(table, where, 5, null, cacheKey, order);
	}
	public List<MRecord> get_pager_c(String table, String where,
			 String cacheKey,Integer page_size, Object... order) {
		return get_pager_p(table, where, page_size, null, cacheKey, order);
	}

	public List<MRecord> get_pager_c(String table, String where,
			Integer page_seg, String cacheKey, Object... order) {
		return get_pager_p(table, where, null, page_seg, cacheKey, order);
	}

	public List<MRecord> get_pager_c(String table, String where,
			Integer pageSize, Integer page_seg, String cacheKey,
			Object... order) {
		return get_pager_p(table, where, pageSize, page_seg, cacheKey, order);
	}

	private List<MRecord> get_pager_p(String table, String where,
			Integer pageSize, Integer page_seg, String cacheKey,
			Object... order) {
		List<MRecord> lst = null;
		if (!MCheck.isNull(cacheKey)) {
			lst = C.getCache(cacheKey);// 获取缓存
		}
		if (MCheck.isNull(lst)) {
			int t_count = get_count(table, where, order);
			int total = MCheck.isNull(t_count) ? 0 : t_count;
			int psize = MCheck.isNull(pageSize) ? 8 : pageSize;
			int current = getCurrenPage(page_seg);
			current = current > total ? 0 : current;
			// 分页写入页面
			MR.me().setAttr("pageInfo",
					MDividPage.getPageLink(current, psize, total, 3, page_seg));
			lst = db.pager(table, where, null, (current - 1) * psize, psize,
					order);
			if (!MCheck.isNull(cacheKey)) {
				C.setCache(lst, cacheKey);
			}// 写入缓存
		}
		return lst;
	}

	private List<MRecord> get_pager_s(String sql, Integer pageSize,
			Integer page_seg) {
		int t_count = get_count_sql(sql);
		int total = MCheck.isNull(t_count) ? 0 : t_count;
		int psize = MCheck.isNull(pageSize) ? 10 : pageSize;
		int current = getCurrenPage(page_seg);
		current = current > total ? 0 : current;
		// 分页写入页面
		MR mr = MR.me();
		String page_str = MDividPage.getPageLink(current, psize, total, 3,
				page_seg);
		mr.setAttr("pageInfo", page_str);
		List<MRecord> lst = db.pager_sql(sql, (current - 1) * psize, psize);
		return lst;
	}

	public List<MRecord> get_pager_sql(String sql) {
		return get_pager_s(sql, null, null);
	}

	public List<MRecord> get_pager_sql(String sql, Integer pageSize) {
		return get_pager_s(sql, pageSize, null);
	}

	// ---------------------分页操作结束
	/**
	 * 返回json给 Extjs 2012-10-23
	 */
	public void get_pager_json(String table, String where, int start,
			int limit, Object... order) {
		int t_count = get_count(table, where);
		List<MRecord> lst = db.pager(table, where, null, start, limit, order);
		String json = "{results:" + t_count + ",rows:"
				+ JSONObj.toJSONString(lst) + "}";
		MCommHelp.outHTML(json);
	}

	public void get_pager_json(String table, String where, String field,
			int start, int limit, Object... order) {
		int t_count = get_count(table, where);
		List<MRecord> lst = db.pager(table, where, field, start, limit, order);
		String json = "{results:" + t_count + ",rows:"
				+ JSONObj.toJSONString(lst) + "}";
		MCommHelp.outHTML(json);
	}

	/**
	 * 返回json给 Extjs 2012-10-23
	 */
	public void get_pager_json(String sql, Integer start, Integer limit) {
		int t_count = get_count_sql(sql);
		List<MRecord> lst = db.pager_sql(sql, start, limit);
		String json = "{results:" + t_count + ",rows:"
				+ JSONObj.toJSONString(lst) + "}";
		MCommHelp.outHTML(json);
	}

	public int get_count_sql(String sql) {
		String sqls = " select count(*) rows from ( " + sql + " )z ";
		MRecord r = db.one_s(sqls);
		if (!MCheck.isNull(r)) {
			return r.getInt("rows");
		}
		return 0;
	}

	public int get_count(String table, String where, Object... order) {
		MRecord r = db.one_t(table, where, "count(*) rows", true, order);
		if (!MCheck.isNull(r)) {
			return r.getInt("rows");
		}
		return 0;
	}

	public boolean tx(ITX tx) {
		return db.tx(tx);
	}

	public boolean check(String table, String where) {
		MRecord r = one_t(table, where);
		return MCheck.isNull(r) ? false : true;
	}
}