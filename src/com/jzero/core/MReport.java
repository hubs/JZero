
package com.jzero.core;

import java.io.File;

import com.jzero.db.cache.MCache;
import com.jzero.util.MCheck;
import com.jzero.util.MDate;

/** 
 * 2012-10-3: 参照JFinal,用于将当前操作显示到控制台,需要借助AOP注解完成
 * wangujqw@gmail.com
 */
public class MReport {
	public static final void doSQLReport(String sql,Object... uris){
		StringBuilder sb = new StringBuilder("\nJZero SQL report ###### ").append(MDate.get_ymd_hms()).append(" ##########################\n");
		sb.append("sql=>    :").append(sql).append("\n");
		
		if(!MCheck.isNull(uris)){
			sb.append("UrlPara     : ").append("\n");
			for(Object str:uris){
				sb.append("\t \t \t \t   uri     : ").append(str).append("\n");
			}
		}
		sb.append("######################################################################\n");
		System.out.print(sb.toString());		
	}
	public static final void doFileReport(String sql){
		File file=MCache.me().get_path(sql);
		StringBuilder sb = new StringBuilder("\nJZero File report ###### ").append(MDate.get_ymd_hms()).append(" ##########################\n");
		sb.append("正在读取缓存文件=>").append(file.getName()).append("\n");
		sb.append("sql=>    :").append(sql).append("\n");
		sb.append("#######################################################################\n");
		System.out.print(sb.toString());		
	}
	public static final void doErrorReport(String path,String controller,String method,String... uris) {
		StringBuilder sb = new StringBuilder("\nJZero NOT FIND ACTION report-------- ").append(MDate.get_ymd_hms()).append(" ------------------------------\n");
		sb.append("Package    :").append(path);
		sb.append("\nController  : ").append(controller).append(".java");
		sb.append("\nMethod      : ").append(method).append("()").append("\n");
		sb.append("\ntarget      : ").append(MInit.get().uri()+"\n");
		if(!MCheck.isNull(uris)){
			sb.append("UrlPara     : ").append("\n");
			for(String str:uris){
				sb.append("\t \t \t \t   uri     : ").append(str).append("\n");
			}
		}
		
		sb.append("--------------------------------------------------------------------------------\n");
		System.out.print(sb.toString());	
	}
	public static final void doReport(String path,String controller,String method,String... uris) {
		StringBuilder sb = new StringBuilder("\nJZero action report -------- ").append(MDate.get_ymd_hms()).append(" ------------------------------\n");
		sb.append("Package    :").append(path);
		sb.append("\nController  : ").append(controller).append(".java");
		sb.append("\nMethod      : ").append(method).append("()").append("\n");
		sb.append("\ntarget      : ").append(MInit.get().uri()+"\n");
		if(!MCheck.isNull(uris)){
			sb.append("UrlPara     : ").append("\n");
			for(String str:uris){
				sb.append("\t uri     : ").append(str).append("\n");
			}
		}
		sb.append("--------------------------------------------------------------------------------\n");
		System.out.print(sb.toString());
	}
}
