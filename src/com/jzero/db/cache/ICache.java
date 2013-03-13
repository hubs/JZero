package com.jzero.db.cache;

import java.io.File;
import java.util.List;

import com.jzero.util.MRecord;
/**
 * 2012-10-3:此类主要用于将查询出来的数据写入文件中,当下次查询相同的数据时,直接从文件中读取显示
 */
public interface ICache {
	File get_path(String sql);

	void write(String sql, List<MRecord> lst);
	public List<MRecord> read(String sql);
	
	void delete(String sql);

	public void delete_all();

	

}
