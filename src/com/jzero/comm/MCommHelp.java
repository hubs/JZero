 
package com.jzero.comm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.jzero.core.MR;
import com.jzero.core.MURI;
import com.jzero.db.utility.MysqlDb;
import com.jzero.log.Log;
import com.jzero.util.MCheck;
import com.jzero.util.MCnt;
import com.jzero.util.MDate;
import com.jzero.util.MEnum;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;
import com.jzero.util.Msg;

/** 
 * 2012-10-7: MComm.java辅助类
 * wangujqw@gmail.com
 */
public class MCommHelp {
	/**
	 * @param url welcome/list
	 * @param msg js 弹出的信息
	 */
	public static void outHTML(String url,String msg) {
		outHTML_comm(msg, url);
	}
	public static void outHTML_dialog(String msg) {
	    outHTML_comm(msg, null);
	}
	public static void outHTML(String msg){
		PrintWriter printWriter;
		try {
			printWriter = MR.me().getResponse().getWriter();
			printWriter.write(msg);
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			Log.me().write_error(e);
		}				
	}
	private  static void outHTML_comm(String msg,String url){
		url=MR.me().getToURI(url);
		PrintWriter printWriter;
		try {
			printWriter = MR.me().getResponse().getWriter();
			StringBuffer html=new StringBuffer("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><title>提示</title>");
			html.append("<link rel='stylesheet' type='text/css' href='"+MTool.getBase()+"/js/lhgdialog/skins/idialog.css'/>")
			.append("<script type='text/javascript' src='"+MTool.getBase()+"/js/jquery.js' ></script>")
			.append("<script type='text/javascript' src='"+MTool.getBase()+"/js/lhgdialog/lhgdialog.min.js'></script>")
			.append("</head><body><script type=\"text/javascript\">")
			.append("$.dialog.alert('"+msg+"'");
			if(!MCheck.isNull(url)){
				html.append(" ,function(){ location='"+url+"'});");
			}
			html.append("</script></body></html>");
			printWriter.write(html.toString());
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			Log.me().write_error(e);
		}		
	}
	/**
	 * 读取display表字段值
	 * @return
	 */
	private static List<MRecord> getDefaultField(String tableName){
		return MysqlDb.getIns().getCommentAndFieldName(tableName);
	}
	/**
	 * 解析find_str:/welcome(0)/find(1)/order_str(2)/find_str(3)字符串,存放格式为 cxzd(查询字段),cxnr(查询内容),begintime(开始时间),endtime(截止时间),select_name(选择的下拉框值)
	 * @return 
	 */
	private static MRecord getFindValueByURI(Integer index){
		index=MCheck.isNull(index)?3:index;
		String find_v=MURI.me().seg_str(index);
		String select_nm=MR.me().getPara("select_name");
		MRecord record=new MRecord();
		record.set("select_name", select_nm);
		if(!MCheck.isNull(find_v)){
			if("ALL".equalsIgnoreCase(find_v)){ // all,时执行
				record.set("cxzd", find_v,"ALL");
				record.set("cxnr", null);
			}else{
				String strs[]=find_v.split(",");
				String cxzd=strs[0];
				if(cxzd.equalsIgnoreCase("ALL")){//all,'',2012-10-7,2012-10-7,未审核
					if(strs.length==4){//all,'',2012-10-7,2012-10-7
						record.set("btime", strs[2],MDate.get_min_day());
						record.set("etime", strs[3],MDate.get_max_day());
					} if(strs.length==5){////all,'',2012-10-7,2012-10-7,未审核
						record.set("btime", strs[2],MDate.get_min_day());
						record.set("etime", strs[3],MDate.get_max_day());
						record.set("select_name",strs[4]);
					}
				}else{							 //bh,'001',2012-10-7,2012-10-7,未审核
					if(strs.length==4){ //bh,'001',2012-10-7,2012-10-7
						record.set("cxzd", strs[0],"ALL");
						record.set("cxnr", MTool.UTF8(strs[1]));
						record.set("btime", strs[2],MDate.get_min_day());
						record.set("etime", strs[3],MDate.get_max_day());
					}else if(strs.length==5){
						record.set("cxzd", strs[0],"ALL");
						record.set("cxnr", MTool.UTF8(strs[1]));
						record.set("btime", strs[2],MDate.get_min_day());
						record.set("etime", strs[3],MDate.get_max_day());
						record.set("select_name",strs[4]);
					}
				}
				if(strs.length==2){
					record.set("cxzd", strs[0]);
					record.set("cxnr", MTool.UTF8(strs[1]));
				}
			}
		}else{
			record.set("cxzd", MR.me().getPara("cxzd"),"ALL");
			record.set("cxnr", MR.me().getPara("cxnr"));
			record.set("btime",MR.me().getPara("btime"), MDate.get_year()+"-01-01");//,
			record.set("etime",MR.me().getPara("etime"),MDate.get_max_day());//
		}
	
		
		return record;
	}
	/**
	 * 读取list 页面的共同信息(下拉框值,时间框)
	 */
	public static MRecord get_common_list_value(String tableName,Integer seq){
		MRecord record=getFindValueByURI(seq);
		MR.me().setAttr(Msg.OBJECT, record);
		MR.me().setAttr(Msg.FIND_STR, record.get("cxzd")+","+record.get("cxnr")+","+record.getStr("btime")+","+record.getStr("etime")+","+record.getStr("select_name"));
		record.set("lst", getDefaultField(tableName));
		return record;
	}
	public static void get_common_list_value(String tableName){
		MRecord record= get_common_list_value(tableName, null);
		MR.me().setAttr(Msg.OBJECT, record);
	}
	/**
	 * 
	 * @param timeField 时间字段
	 * @param select_name 如果有下拉框,则是select 的名称
	 * @param seq uri中的第几位 welcome(0)/index(1)/xx
	 * @return
	 */
	public static String get_find_where(String timeField,String select_name,Integer seq){
		MRecord record=getFindValueByURI(seq);
		MR.me().setAttr(Msg.FIND_STR, record.get("cxzd")+","+record.get("cxnr")+","+record.getStr("btime")+","+record.getStr("etime")+","+record.getStr("select_name"));
		return getWhereFieldByDefault(timeField,select_name,record);
	}
	public static String get_find_where(String timeField,String select_name){
		return get_find_where(timeField,select_name, 3);
	}
	public static String get_find_where(String timeField){
		return get_find_where(timeField,null, 3);
	}
	/**
	 * @param select_name 列表页面进行检索的下拉字段,如,未审核,同意,已审核
	 */ 
	private static String getWhereFieldByDefault(String timeField,String select_name,MRecord record){
		String where="";
		if(!MCheck.isNull(timeField)){//列表页面没有时间检索
			where=getTimesByURI(timeField, record);
		}
		if(!MCheck.isNull(record)){
			where+=getFieldLike(record.getStr("cxzd"), record.getStr("cznr"));
			where+=getSelectEQ(select_name,record.get("select_name"));
		}
		return where;
		
	}
	private static String getSelectEQ(String zt,Object select_name) {
		return !MCheck.isNull(select_name)&&!MCheck.isNull(zt)?MCnt.me().and(zt, MEnum.EQ, select_name).toStr():"";
	}

	private static String getFieldLike(String cxzd,String cxnr){
		String where="";
		if(MCheck.isNull(cxzd)){
			cxzd=MR.me().getPara("cxzd");
		}
		if(MCheck.isNull(cxnr)){
			cxnr=MR.me().getPara("cxnr");
		}
		if(!MCheck.isNull(cxzd)&&!MCheck.isNull(cxnr)&&!"ALL".equalsIgnoreCase(cxzd)){
			where=MCnt.me().like(cxzd, cxnr).toStr();
		}
		return where;
		
	}
	private static String getTimesByURI(String timeField,MRecord record){
		String btime=MR.me().getPara("btime");
		String etime=MR.me().getPara("etime");
		if(MCheck.isNull(btime)&&MCheck.isNull(etime)&&!MCheck.isNull(record)){
			btime=record.getStr("btime");
			etime=record.getStr("etime");
		}
		MCnt cnt=MCnt.me();
		if(!MCheck.isNull(btime)){
			cnt.and(timeField, MEnum.GT_E, btime);
		}
		if(!MCheck.isNull(etime)){
			cnt.and(timeField, MEnum.LT_E, etime);
		}
		return cnt.toStr();
	}
	/**
	 * 
	 * @param orders 如果在list页面点击了排序, 则会替换掉原先的排序
	 * @return
	 */
	public static Object[] getOrderBy(Integer seg,Object ...orders){
		String order=MURI.me().seg_str(seg);
		if(!MCheck.isNull(order)){
			String strs[]=order.split("_");
			if(strs.length>1){
					orders=new Object[]{"order by "+strs[0]+" "+strs[1]};
			}
		}
		MR.me().setAttr(Msg.ORDER_STR,order);
		return orders;
	}
}
