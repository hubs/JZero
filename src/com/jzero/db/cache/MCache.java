
package com.jzero.db.cache;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.util.MCheck;
import com.jzero.util.MMD5;
import com.jzero.util.MPath;
import com.jzero.util.MPrint;
import com.jzero.util.MPro;
import com.jzero.util.MRecord;

/** 
 * 2012-10-3 ,参照CI,将查询出来的结果缓存到文件目录下
 */
public class MCache implements ICache{
	private boolean enabled;
	private static MCache cache=new MCache();
	private static boolean isload=false;
	private MCache(){}
	
	public static MCache me(){
		return cache;
	}
	public void init(){
		setEnabled(MPro.me().getBool("cache_on"));
		isload=true;
	}
	public  File get_path(String sql) {
		File file=null;
		if(!isload){init();}
		if(isEnabled()){//开启了缓存,现在创建目录
			String filename=get_file_name(sql);
			String controler=MInit.get().getURI().seg_str(0);
		
			String cache_file = MPath.me().getSrcPath() + "/cache";
			controler=MCheck.isNull(controler)?MInit.get().getRouter().getDefault_controller():controler;
			file=MFile.createFile(cache_file+"/"+controler.toLowerCase(),filename);//File.separator
		}
		return file;
	}
	public void write(String sql, List<MRecord> lst) {
		File file=get_path(sql);
		if(!MCheck.isNull(file)){//不为空,则写入
			WriteThread thread=new WriteThread(file, lst);
			try {
				pool.submit(thread).get();
			} catch (Exception e) {
				MPrint.print(" Write file Error ");
				Log.me().write_error(e);
			}
		}
	} 
	private class WriteThread extends Thread{
		private final File file;
		private final List<MRecord> lst;
		public WriteThread(File file,List<MRecord> lst){
			this.file=file;
			this.lst=lst;
		}
		@Override
		public void run() {
			MFile.write(file, lst);
		}
		
	}
	
	public void delete(String sql) {
		File file=get_path(sql);
		if(!MCheck.isNull(file)){
			file.delete();
		}
	}
	private String get_file_name(String sql){
		String file_name=MMD5.toMD5(sql);//MEncrypt.encrypt(sql);
		return file_name.length()>258?file_name.substring(0,258):file_name; //2012-10-11,当长度为288时,createFile时就会抛出异常:文件名、目录名或卷标语法不正确
	}
	private static ExecutorService pool=Executors.newCachedThreadPool();
	public void delete_all() {
		String controler=MInit.get().getURI().seg_str(0);
		String cache_file = MPath.me().getWebRoot() + "cache";
		controler=MCheck.isNull(controler)?MInit.get().getRouter().getDefault_controller():controler;
		String path=cache_file+"/"+controler;//File.separator
		DelThread thread=new DelThread(path);
		try {
			pool.submit(thread).get();
		} catch (Exception e) {
			Log.me().write_error(e);
		}
	}
	public void deleta_all_cache(){
		String path = MPath.me().getWebRoot() + "cache";
		DelThread thread=new DelThread(path);
		try {
			pool.submit(thread).get();
		} catch (Exception e) {
			Log.me().write_error(e);
		}		
	}
	private class DelThread extends Thread{
		private final String del_path;
		public DelThread(String path){
			this.del_path=path;
		}
		@Override
		public void run() {
			delete_file(del_path);
		}
	}
	public void delete_file(String file_path){
		File file=new File(file_path);
		if(file.exists()){
			File[] files=file.listFiles();
			for(File f:files){
				if(f.isDirectory()){
					delete_file(f.getAbsolutePath());
				}else{
					boolean bool=f.delete();
					MPrint.print("["+bool+"]clear cache file : "+f.getName());
				}
			}
		}
	}
	public List<MRecord> read(String sql) {
		File file=get_path(sql);
		return MFile.read(file);
	}


	public boolean isEnabled() {
		init();
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
