package com.jzero.tag;

import java.util.List;

import com.jzero.core.MInit;
import com.jzero.core.MR;
import com.jzero.token.MToken;
import com.jzero.util.MCheck;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;
import com.jzero.util.Msg;

/**
 * 2012-10-6: 定义了HTML form相关标签,参考于CI->form_helper.php wangujqw@gmail.com
 */
public class T {
	/**
	 * form 标签
	 */
	private static String form_open(String action, boolean mul,String name,String method) {
		String project_name = MInit.get().getMr().getProject();
		action = "/"+ MTool.firstCharToLowerCase(action);
		if (!action.contains(project_name)){
				if(!MCheck.isNull(action)) {
					action = project_name +action;
				}
		}
		StringBuffer html =new StringBuffer("<form action='" + action+ "' method='post' ");
		if(MCheck.isNull(name)){
			html.append("id = 'niceform'  name='niceform'");
		}else{
			html.append(" id='"+name+"' name='"+name+"'");
		}
		if(!MCheck.isNull(method)){
			html.append(" onsubmit='"+method+"'");
		}
		
		if (mul) {html.append(" enctype='multipart/form-data' ");} 
		html.append(" >");
		// 加CSRF
		MToken token = MInit.get().getToken();
		if (token.isProtection()) {
			html.append(form_hidden(token.getCsrf_toke_name(),token.csrf_set_hash()));
		}
		return html.toString();
	}
	public static String form_open(String action) {
		return form_open(action, false,null,null);
	}
	public static String form_open_mul(String action){
		return form_open(action, true,null,null);
	}
	public static String form_open(String action,String name){
		return form_open(action,false,name,null);
	}
	public static String form_open_m(String action,String method){
		return form_open(action,false,null,method);
	}
	public static String form_open_m(String action,String name,String method){
		return form_open(action,false,name,method);
	}	
	public static String form_hidden(String name,String value){
		return "<input type='hidden' name='"+name+"' value='"+value+"' />";
	}
	public static String form_close(){
		return "</form>";
	}
	
	/**
	 * input 标签
	 * must:是否必须
	 * text:分为text跟password二类
	 * style:css 样式
	 * intValue:是否是数字
	 */
	private static String input(String name,String value,boolean must,boolean text,String style,boolean intValue,String other){
		StringBuffer html=new StringBuffer("<input ");
		html.append(text?" type='text' ":" type='password' ");
		html.append(" name='"+name+"' id='"+name+"'");
		if(!MCheck.isNull(value)){html.append(" value='"+value+"'");}
		if(must){html.append(" class='validate[required]'");}
		if(!MCheck.isNull(style)){html.append(" style='"+style+"'");}
		if(intValue){
			html.append(" alt='msk'");
		}
		if(!MCheck.isNull(other)){
			html.append(other);
		}
		html.append("/>");
		return html.toString();
	}
	public static String input(String name){
		return input(name,null,false,true,null,false,null);
	}
	public static String input_other(String name,String other){
		return input(name,null,false,true,null,false,other);
	}
	public static String input(String name,String value){
		return input(name,value,false,true,null,false,null);
	}
	public static String input_other(String name,String value,String other){
		return input(name,value,false,true,null,false,other);
	}
	public static String input_must(String name){
		return input(name,null,true,true,null,false,null);
	}
	public static String input_must_int(String name){
		return input(name,null,true,true,null,true,null);
	}
	public static String input_must(String name,String value){
		return input(name,value,true,true,null,false,null);
	}
	public static String input_style(String name,String value,String style){
		return input(name,value,false,true,style,false,null);
	}
	public static String input_style_int(String name,String value,String style){
		return input(name,value,false,true,style,true,null);
	}
	public static String input_style(String name,String style){
		return input(name,null,false,true,style,false,null);
	}
	public static String input_style_int(String name,String style){
		return input(name,null,false,true,style,true,null);
	}
	public static String input_must_style(String name,String value,String style){
		return input(name,value,true,true,style,false,null);
	}
	public static String input_must_style(String name,String style){
		return input(name,null,true,true,style,false,null);
	}
	public static String password(String name,String value){
		return input(name,value,true,false,null,false,null);
	}
	public static String password_style(String name,String value,String style){
		return input(name,value,true,false,style,false,null); 
	}
	public static String hidden_id(String value){
		return " <input name='id'   type='hidden' id='id'  value='"+value+"' />";
	}
	public static String hidden(String name,String value){
		return " <input name='"+name+"'   type='hidden' id='"+name+"'  value='"+value+"' />";
	}

	

	/**
	 * 文本标签
	 */
	public static String textarea(String name,String value,String attribute){
		StringBuffer html=new StringBuffer("<textarea name='"+name+"' rows='5' cols='40' ");
		if(!MCheck.isNull(attribute)){
			html.append(attribute);
		}
		html.append(">");
		if(!MCheck.isNull(value)){
			html.append(value);
		}
		return html.append("</textarea>").toString();
	}
	public static String textarea_kind_yes(String name,String value){
		return textarea_kind_no(name,value,null)+
		"<div class=\"content_attr\"><input type=\"text\" style=\"width: 60px;\"  name='snippet' value=\"200\" size=\"3\" class=\"input_align\"/>字符至内容摘要"+
		"<input type=\"text\" name=\"auto_thumb_no\" value=\"1\" size=\"2\" style=\"width: 60px;\" class=\"input_align\"/>张图片作为标题图片</div>";
	}
	public static String textarea_kind_yes(String name){
		return textarea_kind_yes(name,null);
	}
	/**
	 * 没有截止摘要与图片
	 */
	public static String textarea_kind_no(String name,String value,Integer height){
		height=MCheck.isNull(height)?300:height;
		StringBuffer html=new StringBuffer("<textarea name='"+name+"'  style=\"width:96%;height:"+height+"px;visibility:hidden;\">");
		if(!MCheck.isNull(value)){
			html.append(value);
		}
		html.append("</textarea>");
		return html.toString();
	}
	public static String textarea_kind_no(String name){
		return textarea_kind_no(name,null,null);
	}
	public static String textarea_kind_no(String name,int height){
		return textarea_kind_no(name,null,height);
	}
	public static String textarea_kind_no(String name,String value){
		return textarea_kind_no(name,value,null);
	}
	public static String textarea(String name){
		return textarea(name, null, " cols='40' rows='10'");
	}
	public static String textarea(String name,String value){
		return textarea(name, value, " cols='40' rows='10'");
	}

	/**
	 *下拉条标签
	 */
	private static String select(String name,String value,String otherid,String info,String infoValue,List<MRecord> lst,boolean must){
		StringBuffer html=new StringBuffer("<select ");
		if(!MCheck.isNull(name)){ html.append(" name='"+name+"' id='"+name+"'");}
		if(!MCheck.isNull(otherid)){ html.append(" onchange=\"select_name('"+name+"','"+otherid+"')\"" );}
		if(must){html.append("  class='validate[required]'");}
		String msg="全部信息";
		if(!MCheck.isNull(info)){msg=info;}
		html.append(" ><option value=\"");
		if(!MCheck.isNull(infoValue)){
			html.append(infoValue);
		}
		html.append("\">"+msg+"</option>");
		if(!MCheck.isNull(lst)){
			if(MCheck.isNull(value)){
				for(MRecord r:lst){
					html.append("<option value='"+r.getStr("dm")+"'>"+r.getStr("mc")+"</option>");
				}
			}else{
				for(MRecord r:lst){
					String checked=r.getStr("dm").equalsIgnoreCase(value)?" selected=\"selected\" ":"";
					html.append("<option value='"+r.getStr("dm")+"' "+checked+" >"+r.getStr("mc")+"</option>");
				}
			}
		}
		html.append("</select>");
		return html.toString();
	}
	public static String select(String name,String value,List<MRecord> lst){
		return select(name, value, null,null,null, lst,true);
	}
	public static String select(String name,List<MRecord> lst){
		return select(name,null,null,null,null,lst,true);
	}
	public static String select(String name,String value,String info,List<MRecord> lst){
		return select(name,null,null,info,null,lst,true);
	}
	public static String select_info(String name,String info,String infoValue,List<MRecord> lst){
		return select(name,null,null,info,infoValue,lst,true);
	}
	public static String select_info(String name,String value,String info,String infoValue,List<MRecord> lst){
		return select(name,value,null,info,infoValue,lst,true);
	}
	//新增的时候使用
	public static String select_other(String name,String otherid,List<MRecord> lst){
		return select(name,null,otherid,null,null,lst,true);
	}
	//修改时使用
	public static String select_other(String name,String value,String otherid,List<MRecord> lst){
		return select(name,value,otherid,null,null,lst,true);
	}	
	public static String select_no_must(String name,List<MRecord> lst){
		return select(name,null,null,null,null,lst,false);
	}
	public static String select_no_must(String name,String value,List<MRecord> lst){
		return select(name,value,null,null,null,lst,false);
	}	
	/**
	 * label标签
	 */
	public static String label(String text,String forid){
		return "<label for='"+forid+"'>"+text+"</label>";
	}

	
	/**
	 * fieldset 标签
	 */
	public static String fieldset(String text){
		return "  <fieldset><legend>"+text+"</legend>";
	}
	public static String fieldset_close(){
		return "  </fieldset>";
	}
	
	/**
	 * list table 标签
	 */
	public static String table_list(int colspan,String title){//#d1ddaa
		return "<table width='98%' border='0' cellpadding='2' cellspacing='1' class='list' bgcolor='#D1DDAA' align='center' style='margin-top:8px'><tbody><tr><td  colspan="+colspan+" class='title'>&nbsp;【"+title+"】&nbsp;</td></tr>";
	}
	public static String table_close(){
		return "</tbody></table>";
	}
	public static String table_close_form(){
		return "</tbody></table></form>";
	}
	private static String table_other(int colspan,String title){
		return "<table width='98%'  border='0' cellpadding='2' cellspacing='1' class='other' bgcolor='#D1DDAA' align='center' style='margin-top:8px'><tr bgcolor='#E7E7E7'><td  colspan='"+colspan+"' class='title'>【"+title+"】</td></tr>";   
	}
	public static String table_add(int colspan){
		return table_other(colspan,"新增页面");
	}
	public static String table_edit(int colspan){
		return table_other(colspan,"修改页面");
	}
	public static String table_view(int colspan){
		return table_other(colspan,"显示页面");
	}
	
	/**
	 * 按钮标签
	 */
	private static String button(String text,String type,String method){
		StringBuffer html=new StringBuffer(" <input type='"+type+"' value='"+text+"' class='coolbg np' ");
		if(!MCheck.isNull(method)){
			html.append(" onclick=\""+method+"\"");
		}
		html.append(" />");
		return html.toString();
	}
	public static String submit(String text){
		return button(text,"submit",null);
	}
	public static String button(String text,String method){
		return button(text,"button",method);
	}
	
	/**
	 * tr 标签
	 */
	public static String tr(String align,String style){
		StringBuffer html=new StringBuffer("<tr");
		if(!MCheck.isNull(align)){
			html.append(" align='"+align+"'");
		}
		if(!MCheck.isNull(style)){
			html.append(" style='"+style+"'");
		}
		html.append(">");
		return html.toString();
	}
	public static String tr(){ return tr(null,null);}
	public static String tr_align(String align){return tr(align,null);}
	public static String tr(String style){return tr(null,style);}
	public static String tr_close(){ return "</tr>";}
	public static String tr_space(Integer colspan){
		return "<tr ><td height='8' colspan='"+colspan+"' class='title' ></td></tr>";
	};
	public static String tr_space(){
		return tr_space(2);
	}
	
	/**
	 * td标签
	 */
	public static String td(Integer colspan,String align,String text,String width,String label,boolean must,boolean ellipsis){
		StringBuffer html=new StringBuffer("<td ");
		if(!MCheck.isNull(colspan)){
			html.append(" colspan='"+colspan+"'");
		}
		if(!MCheck.isNull(width)){
			html.append(" width='"+width+"'");
		}
		if(!MCheck.isNull(align)){
			html.append(" align='"+align+"'");
		}
		html.append(">");
		if(!MCheck.isNull(text)){
			if(!MCheck.isNull(label)){
				html.append(label(text, label));
			}else{
				if(ellipsis){
					html.append("<abbr title='"+text+"' class='ellipsis'>"+text+"</abbr>");
				}else{
					html.append(text);
				}
			}
		}
		if(must){
			html.append("<span style='color:red;padding-left:10px'>*</span>");
		}
		html.append("</td>");
		return html.toString();
	}
	public static String td(){return "<td>";}
	public static String td_s(Integer colspan){return "<td colspan='"+colspan+"'>";}
	public static String td_close(){ return "</td>";}
	public static String td(String width,String text){ return td(null,null,text,width,null,false,false);}
	public static String td(String text){return td(null,null,text,null,null,false,false);}
	public static String td_e(String text){return td(null,null,text,null,null,false,true);}
	public static String td(int colspan,String text){return td(colspan,null,text,null,null,false,false);}
	public static String td(String align,String text,String width){return td(null,align,text,width,null,false,false);}
	public static String td_c(String align){return "<td align='"+align+"' width='15%'>";}
	public static String td_c(String align,String text){return "<td align='"+align+"'>"+text+"</td>";}
	public static String td_c(String align,String text,String width){return "<td align='"+align+"' width='"+width+"'>"+text+"</td>";}
	public static String td_l(String width,String text,String label){ return td(null,null,text,width,label,false,false);}
	public static String td_l(String text,String label){ return td(null,null,text,null,label,false,false);}
	public static String td_l_must(String width,String text,String label){ return td(null,null,text,width,label,true,false);}
	public static String td_l_must(String text,String label){ return td(null,null,text,null,label,true,false);}
	/**
	 * 链接标签(排序使用)
	 */
	public static String a_o(String field,String param){
		String uri= MTool.get_path()+"/index";
		uri=MCheck.isNull(param)?uri:uri+"/"+param;
		return "<a id='"+field+"' href=\"javascript:;\" onclick=\"orderby(this.id,'"+uri+"')\" class=\"desc\"><span class=\"p_l\"></span></a>";	
	}
	public static String a_o(String field){
		return a_o(field,null);
	}
	public static String a_down(String href){
		return "<a href='"+href+"' title='点击下载' target='_blank'>点击下载</a>";
	}
	/**
	 * 键接标签(修改使用)
	 */
	public static String a_edit(String id){
		return a_edit(id, "修改");
	}
	public static String a_edit(String id,String name){
		String uri= MTool.getBase()+MTool.get_path()+"/edit/"+id+"/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		return "<a href='"+uri+"' title='"+name+"' class='coolbg np'>〖"+name+"〗</a>";
	}
	public static String a_edit_submit(String name){
		return " <a href='#' onclick='document.forms[0].submit();return false;'>〖"+name+"〗</a>";
	}
	/**
	 * @param uri menu/edit
	 * 
	 * /xx/xx
	 * @return
	 */
	public static String a_edit_base(String uri){
		String uri_str= MTool.getBase()+uri;
		return "<a href='"+uri_str+"' title=\"修改\">〖修改〗</a>";
	}
	/**
	 * 链接标签(删除使用)
	 */
	public static String a_del(String id){
		String uri= MTool.getBase()+MTool.get_path()+"/del/"+id+"/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		return "<a href='javascript:;' onclick=\"return del('"+uri+"');\" title=\"删除\">〖删除〗</a>";
	}
	public static String button_del(String id){
		String uri= MTool.getBase()+MTool.get_path()+"/del/"+id+"/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		return "<input type=\"button\" class=\"coolbg np\" onclick=\"return del('"+uri+"');\" value=\"删除\">";
	}
	public static String a_audit(String id,String param){
		String uri= MTool.getBase()+MTool.get_path()+"/audit/"+id;
		if(!MCheck.isNull(param)){
			uri+="/"+param;
		}
		uri+="/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		return "<a href='javascript:;' onclick=\"return audit('"+uri+"');\">〖审核〗</a>";
	}
	public static String a_audit(String id){
		return a_audit(id, null);
	}
	/**
	 * @param uri menu/edit/xx/xx
	 * @return
	 */
	public static String a_del_base(String uri){
		String uri_str= MTool.getBase()+uri;
		return "<a href='javascript:;' onclick=\"return del('"+uri_str+"');\" title=\"删除\">〖删除〗</a>";
	}
/**
 	* 查看标签
 */
	public static String a_view(String id,String text,String param){
		String uri= MTool.getBase()+MTool.get_path()+"/view/"+id;
		if(!MCheck.isNull(param)){
			uri+="/"+param;
		}
		uri+="/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		return "【<a href='"+uri+"' title=\"查看\">"+text+"</a>】";		
	}	
	public static String a_view(String id,String text){
		return a_view(id, text,null);
	}
	public static String a(String url,String name){
		name=MCheck.isNull(name)?url:name;
		return "<a href='"+url+"' title='"+name+"' target='_blank'>"+name+"</a>";
	}
	public static String a(String url){
		return a(url,null);
	}
	/**
	 *多选框标签 
	 */
	public static String a_checkbox(){
		return "<input type=\"checkbox\" class=\"input_align\" onclick=\"selectAll(this,'select[]')\"  />";
	}
	/**
	 * 多选框值
	 */
	public static String a_checkbox(String id){
		return a_checkbox(id, true);
	}
	
	//bool=true,则说明可以显示
	public static String a_checkbox(String id,boolean bool){
		String disabled=bool?"":"disabled='true'";
		return "<input type=\"checkbox\" name=\"select[]\"   value='"+id+"' "+disabled+" />";
	}	
	
	
	/**
	 * 内容显示标签
	 */
	public static String text(String field){
		return field;
	}

	/**
	 * 分页标签
	 */
	public static String pager(Integer colspan){
		return pager(colspan, null);
	}
	public static String pager(Integer colspan,String param){
		String uri= MTool.getBase()+MTool.get_path()+"/batch/";
		if(!MCheck.isNull(param)){
			uri+=param;
		}
		uri+="/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		StringBuffer html=new StringBuffer("<tr bgcolor=\"#fafaf1\"><td height='7' colspan="+colspan+"></td></tr><tr  bgcolor='#abb9d7'><td height=36 colspan="+colspan+" >");
		html.append("<input type='button' onclick=\"del_form('delForm','"+uri+"')\" class=\"coolbg np\"  value='批量删除' />");
		html.append(MR.me().getAttr("pageInfo")+"<div style='clear: both'></div>");
		html.append("</td></tr>");
		return html.toString();
	}
	public static String pager_no_del(Integer colspan){//  bgcolor='#abb9d7'
		StringBuffer html=new StringBuffer("<tr><td height=36 colspan="+colspan+" style='padding-top:20px' >");
		html.append(MR.me().getAttr("pageInfo")+"<div style='clear: both'></div>");
		html.append("</td></tr>");
		return html.toString();
	}
	//批量审核
	public static String pager_audit(Integer colspan,String param){
		String uri= MTool.getBase()+MTool.get_path()+"/batch";
		if(!MCheck.isNull(param)){
			uri+="/"+param;
		}
		uri+="/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		StringBuffer html=new StringBuffer("<tr bgcolor=\"#fafaf1\"><td height='7' colspan="+colspan+"></td></tr><tr  bgcolor='#abb9d7'><td height=36 colspan="+colspan+" >");
		html.append("<input type='button' onclick=\"audit_list('delForm','"+uri+"')\" class=\"coolbg np\"  value='批量审核' />");
		html.append(MR.me().getAttr("pageInfo")+"<div style='clear: both'></div>");
		html.append("</td></tr>");
		return html.toString();
	}
	//批量忽略,在留言模块中使用 2012-11-15
	public static String pager_ignore(Integer colspan){
		String uri= MTool.getBase()+MTool.get_path()+"/batch";
		uri+="/"+MR.me().getAttr(Msg.ORDER_STR)+"/"+MR.me().getAttr(Msg.FIND_STR);
		StringBuffer html=new StringBuffer("<tr bgcolor=\"#fafaf1\"><td height='7' colspan="+colspan+"></td></tr><tr  bgcolor='#abb9d7'><td height=36 colspan="+colspan+" >");
		html.append("<input type='button' onclick=\"del_form('delForm','"+uri+"','请选择要忽略的选项.','确定忽略选中的项?')\" class=\"coolbg np\"  value='批量忽略' />");
		html.append(MR.me().getAttr("pageInfo")+"<div style='clear: both'></div>");
		html.append("</td></tr>");
		return html.toString();
	}
	
	
	/**
	 * JS标签
	 */
	public static String js(String mc){
		String url=MTool.getBase()+mc;
		return "<script type=\"text/javascript\" src='"+url+"'\"></script>";
	}
	/**
	 * CSS 标签
	 */
	public static String css(String mc){
		String url=MTool.getBase()+mc;
		return "<link rel=\"stylesheet\" type=\"text/css\" href='"+url+"'>";
	}
	
	public static String end(){
		return "  </body></html>";
	}
	/**
	 * 时间标签
	 */
	public static String time(String name,Object value){
		return "<input class=\"Wdate\" type=\"text\" name='"+name+"' id='"+name+"' value='"+value+"' onfocus=\"WdatePicker({isShowClear:false})\"/>";
	}
	public static String time_must(String name,Object value,boolean ymdhms){
		StringBuffer html=new StringBuffer("<input class=\"Wdate validate[required]\" type=\"text\" name=\""+name+"\" id=\""+name+"\"   ");
		if(!MCheck.isNull(value)){
			html.append(" value = '"+value+"'");
		}
		if(ymdhms){
			html.append("onfocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\"");
		}else{
			html.append("onfocus=\"WdatePicker({isShowClear:false})\"");
		}
		html.append("/>");
		return html.toString();
	}
	public static String time_must(String name,Object value){
		return time_must(name,value,true);
	}
	public static String time_must(String name){
		return time_must(name,null,true);
	}
	public static String time_must_ymd(String name){
		return time_must(name,null,false);
	}
	public static String time_must_ymd(String name,Object value){
		return time_must(name,value,false);
	}
	
	/**
	 * html_top 标签
	 */
	public static String head(String title,String base){
		return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html><head><title>"+title+"</title> <base href='"+base+"'><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> <script type=\"text/javascript\">AJAX_URL='"+base+"';</script>";
	}
	public static String head(String title){
		return head(title,MTool.getBase());
	}
	public static String head_end(){
		return "</head>";
	}
	
	/**
	 * add.jsp table_top
	 */
	public static String add_table(String title,Integer colspan){
		return "<table width=\"98%\"  border=\"0\" cellpadding=\"2\" cellspacing=\"1\" class=\"other\" bgcolor=\"#EEF3F7\" align=\"center\" style=\"margin-top:8px\"><tbody><tr bgcolor=\"#E7E7E7\"><td  colspan='"+colspan+"' class=\"title\">【"+title+"】</td></tr>";
	}
	public static String add_table(){
		return add_table("新增页面",2);
	}
	public static String add_table(int colspan){
		return add_table("新增页面",colspan);
	}
	public static String edit_table(){
		return add_table("修改页面",2);
	}
	public static String edit_table(int colspan){
		return add_table("修改页面",colspan);
	}
	public static String view_table(){
		return add_table("显示页面",2);
	}
	public static String view_table(int colspan){
		return add_table("显示页面",colspan);
	}
	public static String table(String title,int colspan){
		return add_table(title,colspan);
	}
	/**
	 * span
	 */
	public static String span(String text){
		return "<span class=\"explain\">&nbsp;"+text+"</span>";
	}
	
	/**
	 * add.jsp alert js
	 */
	public static String add_alert(String name){
		name=MCheck.isNull(name)?Msg.MESSAGE:name;
		String msg=MR.me().getAttr(name);
		StringBuffer html=new StringBuffer();
		if(!MCheck.isNull(msg)){
			html.append("<script type='text/javascript'> alertMsg('"+msg+"')</script>");
		}
		return html.toString(); 	
	}
	public static String alert(String name){
		return add_alert(name);
	}
	public static String alert(){
		return add_alert(null);
	}
	/**
	 * 2012-10-13:kind js
	 */
	public static String kind_js(String name){
		return "<script type='text/javascript'>var editor;KindEditor.ready(function(K) { editor = K.create('textarea[name="+name+"]', {afterBlur: function(){this.sync();}})});</script>";
	}
	public static String body(){
		return "<body>";
	}
	/**
	 * 上传标签
	 */
	//多图片上传 2012-11-4
	public static String img_mul(){
		return img_mul(2);
	}
	public static String img_mul(Integer colspan){
		int col=colspan;
		return "<tr><td>多图片上传</td><td  colspan='"+col+"'><iframe  frameborder='0' height='120px' width='560px' src='"+MTool.getBase()+"Common/to_mulimg'  name='mulimgiframe' id='mulimgiframe' ></iframe></td></tr><tr><td colspan='"+col+"'><div class='case_list'> <div id='show_img'></div></div></td></tr>";
	}
	public static String img_mul_show(List<MRecord> lst,String current){
		return img_mul_show(lst, current, 2,false);
	}
	public static String img_mul_show(List<MRecord> lst,String current,Integer colspan){
		return img_mul_show(lst, current, colspan,false);
	}
	/**
	 *  只显示图片
	 */
	public static String img_mul_view(List<MRecord> lst){
		return img_mul_view(lst,2);
	}
	public static String img_mul_view(List<MRecord> lst,Integer colspan){
		return img_mul_show(lst,null,colspan,true);
	}
	public static String img_mul_show(List<MRecord> lst,String current,Integer colspan,boolean onlyView){
		int col=colspan;
		StringBuffer sb=null;
		if(onlyView){
			sb=new StringBuffer("<tr><td  colspan='"+col+"'><div class='case_list'> <div id='show_img'>");
		}else{
			sb=new StringBuffer("<tr><td>多图片上传</td><td  colspan='"+col+"'><iframe  frameborder='0' height='120px' width='560px' src='"+MTool.getBase()+"Common/to_mulimg'  name='mulimgiframe' id='mulimgiframe' ></iframe></td></tr><tr><td colspan='"+col+"'><div class='case_list'> <div id='show_img'>");
		}
		if(!MCheck.isNull(lst)){
		for(MRecord row:lst){
			String pathname=row.getStr("path");
			String name=row.getStr("name");
			sb.append("<ul  class='"+name.replace(".","_")+"'><li class='li'><img alt='"+name+"'  width='120px' height='60px' src='"+MTool.getBase()+"upload/"+pathname+"'/></li>");
			if(!onlyView){
				sb.append("<li class='t'>");
				sb.append("<input type='hidden' name='name[]' value='"+name+"'/>");
				sb.append("<input type='hidden' name='path[]' value='"+pathname+"'>");
				sb.append("<input type='radio' name='mrz[]'　class='input_align' value='"+pathname+"'");
				if(!MCheck.isNull(current)&&pathname.equalsIgnoreCase(current)){
					sb.append(" checked='true' ");
				}
				sb.append(" />默认首图");
				sb.append("<a  href='javascript:;' onclick='remove_this(this.id)' title='删除此图片' style='padding-left:15px' id='"+name.replace(".","_")+"' >删除</a>");
				sb.append("</li>");
			}
			sb.append("</ul>");
		  }
		}
		sb.append("</div></div></td></tr>");
		return sb.toString();
	}
	
	public static String img_mul_show(List<MRecord> lst){
		return img_mul_show(lst, null, 2,false);
	}
	//单图片上传
	public static String img_single(String name){
		return img_single(name, null);
	}
	public static String img_single(String name,String path){
		path=MCheck.isNull(path)?"":path;
		return "  <input type='hidden' name='"+name+"' id='logo' value='"+path+"' /><img id='img1' width='220px' height='65' src='"+MTool.getBase()+"upload/"+path+"'/><iframe   frameborder='0' height='80px'  src='"+MTool.getBase()+"Common/to_upimg/logo/img1'  name='imgiframe' id='mgiframe'></iframe>";
	}
	//单图片上传,应射到同一个页面
	public static String img_single_page(String name,String hid,String imgid){
		return "  <input type='hidden' name='"+name+"' id='"+hid+"' /><img id='"+imgid+"' width='220px' height='65' src=''/><iframe   frameborder='0' height='80px'  src='"+MTool.getBase()+"Common/to_upimg/"+hid+"/"+imgid+"'  name='imgiframe' id='mgiframe'></iframe>";
	}
	public static String img_show(String pathname){
		return img_show(pathname,null,null);
	}
	public static String img_show(String pathname,Integer width,Integer height){
		width=MCheck.isNull(width)?220:width;
		height=MCheck.isNull(height)?65:height;
		return "<img  width='"+width+"px' height='"+height+"' src='"+MTool.getBase()+"upload/"+pathname+"'/>";
	}
	
	public static String upload(String name){
		return "<input type=\"file\" name='"+name+"' id='"+name+"'/>";
	}
	
	//根据title自动给tag　赋值
	public static String js_auto(String src,String desc){
		return "<script type='text/javascript'>$(function(){$('#"+src+"').change(function(){var strV=$(this).val();var _tesp=strV.replace(/\\s+/g,'-'); $('#"+desc+"').val(_tesp.replace(/,/g,'-'));$(this).val(_tesp.replace(/,/g,'-'));})})</script>";
	}
	
	//2012-11-18:复选模,为了兼容老的地方使用
	public static String checkbox(String name,String value){
		String checked="";
		if(!MCheck.isNull(value)&&value.equals("是")){checked="checked='true'"; }
		return "<input type='checkbox' name='"+name+"' value='是' "+checked+"   />";
	}
	public static String checkbox_new(String name,String value,String selectValue){
		String checked="";
		if(!MCheck.isNull(selectValue)){
			 String select=selectValue.equals("true")?"1":"0";
			 if(select.equals(value)){checked="checked='true'";}
		}
		return "<input type='checkbox' name='"+name+"' value='"+value+"' "+checked+"   />";
	}
	public static String checkbox_new(String name,String value){
		return checkbox_new(name, value, null);
	}
	public static String checkbox(String name){
		return checkbox(name, null);
	}
	//2013-3-7:单选框
	public static String radio(String name,String value,String selectValue){
		String checked="";
		
		if(!MCheck.isNull(selectValue)){
			 String select=selectValue.equals("true")?"1":"0";
			 if(select.equals(value)){checked="checked='true'";}
		}
		return "<input type='radio' name='"+name+"' value='"+value+"' "+checked+"   />";
	}
	public static String radio(String name,String value){
		return radio(name, value, null);
	}
}
