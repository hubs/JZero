
package com.jzero.comm;

import java.util.List;

import com.jzero.core.MR;
import com.jzero.core.MRouter;
import com.jzero.core.MURI;
import com.jzero.db.core.M;
import com.jzero.render.MB;
import com.jzero.upload.MUpload;
import com.jzero.util.MCheck;
import com.jzero.util.MCnt;
import com.jzero.util.MEnum;
import com.jzero.util.MRecord;
import com.jzero.util.MTool;
import com.jzero.util.Msg;

/** 
 * 2012-10-7: 页面CRUD修改
 * wangujqw@gmail.com
 */
public class MComm {
	private MComm(){}
	private static MComm comm=new MComm();
	public static MComm me(){return comm;}
	private String getDefault_toPath(String toURI){
		MRouter r=MRouter.me();
		String dir=r.getDirectory();
		String con=r.getController();
		String path=dir.equalsIgnoreCase(con)?"/"+con+"/":"/"+dir+"/"+con+"/";
		path+=MCheck.isNull(toURI)?r.getMethod():toURI;
		return path.toLowerCase()+".jsp";
	}
	private String getDefault_Redirect(String toURI,String method){
		if(MCheck.isNull(toURI)){
			MRouter r=MRouter.me();
			String dir=r.getDirectory();
			String con=r.getController();
			String path=dir.equalsIgnoreCase(con)?con+"/":dir+"/"+con;
			toURI=path+"/"+method;
		}
		return toURI;
	}	
	/**
	 * 跳转到列表页面
	 * @param toURI /welcome/list.jsp
	 */
	public void m_list(String table,String toURI,MRecord outDatas,String where,Object ...order){
		m_list(table, toURI, outDatas, where, null,null,null, order);
	}
	//->格式: dir/controller/method/order/find_str/page_seg
	public void m_list(String table,String toURI,MRecord outDatas,String where,Integer page_seg,Integer order_seg,Integer pageSize,Object ...order){
		int level=MTool.get_level();
		order_seg=MCheck.isNull(order_seg)?level==1?2:3:order_seg;
		order=MCommHelp.getOrderBy(order_seg,order);		//排序的字段
		int find_seg=order_seg+1;
		MCommHelp.get_common_list_value(table,find_seg);	//得到下拉框,时间框,等初始值
		page_seg=MCheck.isNull(page_seg)?level==1?4:5:page_seg;
		M.me().get_pager(table, where,pageSize,page_seg, order);	//进行分页显示
		if(!MCheck.isNull(outDatas)){
			MR.me().setAttr(Msg.OUT_DATAS, outDatas);
		}//输出页面的值
		
		toURI=getDefault_toPath(toURI);//得到跳转的URI地址
		MB.me().getJspRender(toURI);			//跳转到JSP页面
	}
	public void m_list(String table,MRecord outDatas,String where,Object ...order){
		 m_list(table,null,outDatas,where,null,null,null,order);
	}
	public void m_list(String table,MRecord outDatas,String where,Integer pager_seg,Object ...order){
		 m_list(table,null,outDatas,where,pager_seg,null,null,order);
	}
	public void m_list(String table,MRecord outDatas,String where,Integer pager_seg,Integer order_seg,Object ...order){
		 m_list(table,null,outDatas,where,pager_seg,order_seg,null,order);
	}	
	public void m_list(String table,MRecord outDatas,Integer pager_seg,String where,Object ...order){
		 m_list(table,null,outDatas,where,pager_seg,null,null,order);
	}	
	public void m_list(String table,Object...order){
		m_list(table, null, null, null,null,null,null,order);
	}
	public void m_list(String table,String where,Integer pageSize,Object...order){
		m_list(table, null, null, where,null,null,pageSize,order);
	}
	public void m_list_no_page(String table,Integer pageSize,Object...order){
		m_list(table, null, null, null,null,null,pageSize,order);
	}	
	public void m_list(String table,String where,Object...order){
		m_list(table, null, null, where,null,null,null,order);
	}
	public void m_list(String table,String where,int order_seg,Object...order){
		m_list(table, null, null, where,null,order_seg,null,order);
	}
	public void m_list(String table,Integer page_seg,Object...order){
		m_list(table, null, null, null,page_seg,null,null,order);
	}
	public void m_list(String table,Integer order_seg,Integer page_seg,Object...order){
		m_list(table, null, null, null,page_seg,order_seg,null,order);
	}
	public void m_list_sql(String sql,String toURI,MRecord outDatas,Integer pageSize,Object ...order){
		
		List<MRecord> lst=M.me().get_pager_sql(sql, pageSize);
		if(!MCheck.isNull(outDatas)){
			MR.me().setAttr(Msg.OUT_DATAS, outDatas);
		}
		MR.me().setAttr(Msg.LIST_DATAS, lst);
		toURI=getDefault_toPath(toURI);
		MB.me().getJspRender(toURI);
	}
	public void m_list_sql(String sql,MRecord outDatas,Object...order){
		m_list_sql(sql,null,outDatas,null,order);
	}

	/**
	 * 查询事件 find
	 */
	public void m_find(String table,String select_name,String toURI,MRecord outDatas,String where,String findField,Integer order_seg,Integer find_seg,Object ...order){
		find_seg=MCheck.isNull(order_seg)?3:order_seg+1;//如果设置了排序字段位置,则find_seg在排序的字段后面,所在加上1  2012-11-02
		String w=MCommHelp.get_find_where(findField,select_name,find_seg);
		if(!MCheck.isNull(where)){w+=where;}
		m_list(table,toURI, outDatas, w,null,order_seg,null,order);
	}
	public void m_find(String table,String where,String select_name,Object...order){
		m_find(table,select_name,null,null,where,null,null,order);
	}
	public void m_find(String table,Object ...order){
		m_find(table,null,"index",null,null,null,null,null,order);
	}
	public void m_find_t(String table,String find_time_field,Object ...order){
		m_find(table,null,"index",null,null,find_time_field,null,null,order);
	}
	public void m_find(String table,Integer find_seg){
		m_find(table,null,"index",null,null,null,null,find_seg,new Object[]{});
	}
	public void m_find(String table,MRecord out,String where,String findField,Object ...order){
		m_find(table,null,"index",out,where,findField,null,null,order);
	}
	public void m_find(String table,MRecord out,String where,String findField,int order_seg,Object ...order){
		m_find(table,null,"index",out,where,findField,order_seg,null,order);
	}	
	/**
	 * 删除操作,uri=>/welcome/del/id/order_str/find_str
	 * @param toURI =>welcome/index
	 * 一般的:url=/proname/controller(0)/method(1)/param(2)/order(3)/find_value(4)
	 * CRUD的url=/proname/controller/find/order/find_value
	 */
	private void m_del(String table,String toUrl,String where,String field,boolean tolist,Integer seg){
		int seg_int=MCheck.isNull(seg)?2:seg;
		if(MCheck.isNull(where)){
			String value=MURI.me().seg_str(seg_int);
			if(MCheck.isNull(field)){
				where=MCnt.me().first("id", MEnum.EQ, value).toStr();
			}else{
				where=MCnt.me().first(field, MEnum.EQ, value).toStr();
			}
		}
		String order_str=MURI.me().seg_str(seg_int+1);//排序的字段
		String find_str=MURI.me().seg_str(seg_int+2);
		int result=M.me().delete(table, where);
		String msg=result>0?Msg.DELETE_SUCCESS:Msg.DELETE_ERROR;
		String path=tolist?getDefault_Redirect(toUrl, "index"):getDefault_Redirect(toUrl,"find/"+order_str+"/"+find_str );
		MCommHelp.outHTML(path,msg);
	}
	public void m_del(String table,boolean tolist){
		m_del(table,null,null,null,tolist,null);
	}
	public void m_del(String table,boolean tolist,String where){
		m_del(table,null,where,null,tolist,null);
	}
	public void m_del(String table,String field,boolean tolist){
		m_del(table,null,null,field,tolist,null);
	}
	public void m_del(String table){
		m_del(table,null,null,null,true,null);
	}
	public void m_del(String table,int seg){
		m_del(table,null,null,null,true,seg);
	}
	public void m_del(String table,String toUrl){
		m_del(table,toUrl,null,null,true,null);
	}
	public void m_del(String table,String toUrl,Integer seg){
		m_del(table,toUrl,null,null,true,seg);
	}
	
	
	/**
	 * 跳转到新增页面 toURI,/welcome/add.jsp
	 * @param record,传到新增页面的数据,一般是下拉框的值
	 */
	public void m_add(String toURI,MRecord record){
		if(!MCheck.isNull(record)){
			MR.me().setAttr(Msg.OUT_DATAS, record);
		}
		toURI=getDefault_toPath(toURI);
//		toURI=MCheck.isNull(toURI)?getDefault_toPath(toURI):toURI;
		MB.me().getJspRender(toURI);
	}
	public void m_add(MRecord record){
		m_add("add",record);
	}
	public void m_add(){
		m_add("add",null);
	}
	public void m_add(String toURI){
		m_add(toURI,null);
	}

	/** 
	 * 保存操作,保存后跳转到列表页面 action=>/welcome/save/order_str/find_str
	 */
	public void m_save(String table,MRecord in,boolean tolist,Integer seg){
		int result=M.me().insert(table, in);
		String msg=result>0?Msg.INSERT_SUCCESS:Msg.INSERT_ERROR;
		seg=MCheck.isNull(seg)?2:seg;
		String order_str=MURI.me().seg_str(seg);//排序的字段
		String find_str=MURI.me().seg_str(seg+1);
		String path=tolist?getDefault_Redirect(null, "index"):getDefault_Redirect(null,"find/"+order_str+"/"+find_str );
		MCommHelp.outHTML(path,msg);
	}
	
	public void m_save(String table,MRecord in,boolean tolist){
		m_save(table,in,tolist,null);
	}
	public void m_save_list(String table,MRecord in){
		m_save(table,in,true,null);
	}
	public void m_save_list(String table,MRecord in,int seg){
		m_save(table,in,true,seg);
	}
	public void m_save_find(String table,MRecord in){
		m_save(table,in,false,null);
	}
	
	/**
	 * 保存操作,保存后跳转到新增页面
	 */
	public void m_save(String table,String toURI,MRecord in,MRecord outDatas){
		int result=M.me().insert(table, in);
		String msg=result>0?Msg.INSERT_SUCCESS:Msg.INSERT_ERROR;
		if(!MCheck.isNull(outDatas)){
			MR.me().setAttr(Msg.OUT_DATAS, outDatas);
		}
		MR.me().setAttr(Msg.MESSAGE, msg);
		toURI=getDefault_toPath(toURI);
		MB.me().getJspRender(toURI);
	}
	public void m_save(String table,String toURI,MRecord in){
		m_save(table,toURI,in,null);
	}
	public void m_save(String table,MRecord in,MRecord outDatas){
		m_save(table,"add",in,outDatas);
	}
	public void m_save(String table,MRecord in){
		m_save(table,"add",in,null);
	}
	public void m_save(String table){
		MRecord record=MR.me().getPara();
		record=MTool.get_img(record);
		record=MTool.get_intro(record);
		m_save(table,"add",record,null);
	}
	public void m_save_upload(String table){
		MRecord record=MUpload.me().upload();
		record=MTool.get_img(record);
		record=MTool.get_intro(record);
		m_save(table,null,record,null);
	}
	/**
	 * 跳转到修改页面 uri->/welcome/edit/id/order_str/find_str
	 */
	public void m_edit(String table,String where,String toURI,String field,MRecord outDatas,Integer seg){
		int seg_int=MCheck.isNull(seg)?2:seg;
		if(MCheck.isNull(where)){
			String value=MURI.me().seg_str(seg_int);
			if(MCheck.isNull(field)){
				where=MCnt.me().and("id", MEnum.EQ, value).toStr();
			}else{
				where=MCnt.me().and(field, MEnum.EQ, value).toStr();
			}
		}
		if(!MCheck.isNull(outDatas)){
			MR.me().setAttr(Msg.OUT_DATAS, outDatas);
		}
		MR.me().setAttr(Msg.OBJECT, M.me().one_t(table, where));
		MR.me().setAttr(Msg.ORDER_STR, MURI.me().seg_str(seg_int+1));
		MR.me().setAttr(Msg.FIND_STR, MURI.me().seg_str(seg_int+2));
//		toURI=MCheck.isNull(toURI)?getDefault_toPath(toURI):toURI;
		toURI=getDefault_toPath(toURI);
		MB.me().getJspRender(toURI);
	}
	public void m_edit(String table){
		m_edit(table, null, null, null, null,null);
	}
	public void m_edit(String table,int seg){
		m_edit(table, null, null, null, null,seg);
	}
	public void m_edit(String table,String where){
		m_edit(table, where, null, null, null,null);
	}
	public void m_edit(String table,MRecord out,int seg){
		m_edit(table, null, null, null, out,seg);
	}
	public void m_edit(String table,MRecord out){
		m_edit(table, null, null, null, out,null);
	}	
	public void m_edit(String table,String where,String toURI){
		m_edit(table, where, toURI, null, null,null);
	}
	public void m_edit(String table,String field,MRecord outDatas){
		m_edit(table,null,null,field,outDatas,null);
	}
	public void m_edit(String table,String toURI,MRecord outDatas,Integer seq){
		m_edit(table,null,toURI,null,outDatas,seq);
	}
	/**
	 * 2012-10-12
	 * @param table
	 * @param toURI 跳转的地址
	 */
	public void m_view(String table,String toURI,Integer id_seg,MRecord out){
		id_seg=MCheck.isNull(id_seg)?2:id_seg;
		String value=MURI.me().seg_str(id_seg);
		if(!MCheck.isNull(out)){
			MR.me().setAttr(Msg.OUT_DATAS, out);
		}
		String where=MCnt.me().and("id", MEnum.EQ, value).toStr();
		MR.me().setAttr(Msg.OBJECT, M.me().one_t(table, where));
		toURI=getDefault_toPath(toURI);
		MB.me().getJspRender(toURI);	
	}
	public void m_view(String table,Integer id_seg){
		m_view(table,"view",id_seg,null);
	}
	public void m_view(String table,Integer id_seg,MRecord out){
		m_view(table,"view",id_seg,out);
	}
	public void m_view(String table){
		m_view(table,"view",null,null);
	}
	public void m_view(String table,String toURI){
		m_view(table,toURI,null,null);
	}
	/**
	 * 更新操作,更新后跳转到列表页面 /welcome/update/order_str/find_str
	 */
	public void m_update(String table,String toURI,MRecord inDatas,boolean tolist,Integer seg){
		String where=MCnt.me().first("id", MEnum.EQ, MR.me().getPara("id")).toStr();
		int result=M.me().update(table, inDatas, where);
		String msg=result>0?Msg.MODIFY_SUCCESS:Msg.MODIFY_ERROR;
		seg=MCheck.isNull(seg)?2:seg;
		String order_str=MURI.me().seg_str(seg);//排序的字段
		String find_str=MURI.me().seg_str(seg+1);
		String path=tolist?getDefault_Redirect(toURI, "index"):getDefault_Redirect(toURI,"find/"+order_str+"/"+find_str );
		MCommHelp.outHTML(path,msg);
	}
	public void m_update(String table,String toURI,MRecord inDatas,boolean tolist){
		m_update(table,toURI,inDatas,tolist,null);
	}
	public void m_update_list(String table,MRecord inDatas){
		m_update(table,null,inDatas,true,null);
	}
	public void m_update_list(String table,int seg){
		m_update(table,null,MR.me().getPara(),true,seg);
	}
	public void m_update_list(String table){
		m_update(table,null,MR.me().getPara(),true,null);
	}
	public void m_update(String table,String toURI){
		m_update(table,toURI,MR.me().getPara(),true,null);
	}
	public void m_update(String table,String toURI,int in_seg){
		m_update(table,toURI,MR.me().getPara(),true,in_seg);
	}
	public void m_update(String table,MRecord inDatas){
		m_update(table,null,inDatas,false,null);
	}
	public void m_update(String table){
		MRecord record=MR.me().getPara();
		record=MTool.get_img(record);
		record=MTool.get_intro(record);
		m_update(table,null,record.remove("id"),false,null);
	}
	public void m_update_upload(String table){
		MRecord record=MUpload.me().upload();
		record=MTool.get_img(record);
		record=MTool.get_intro(record);
		m_update(table,null,record.remove("id"),false,null);
	}

	
	/**
	 * 批量删除操作 2012-10-12
	 */
	public void m_batch_del(String table,String toURI,String select,boolean tolist,Integer seg){
		String[] ids=MR.me().getParaValues(select);
		String where=null;
		int result=0;
		for(String id:ids){
			where=MCnt.me().first("id", MEnum.EQ, id).toStr();
			M.me().delete(table, where);
			result++;
		}
		String msg=result>0?Msg.BATCH_DELETE_SUCCESS:Msg.DELETE_ERROR;
		seg=MCheck.isNull(seg)?2:seg;
		String path=tolist?getDefault_Redirect(toURI, "index"):getDefault_Redirect(toURI,"find/"+MURI.me().seg_str(seg)+"/"+MURI.me().seg_str(seg+1) );
		MCommHelp.outHTML(path,msg);
	}
	public void m_batch_del(String table){
		m_batch_del(table,null,"select[]",true,null);
	}
	public void m_batch_del(String table,String toURI){
		m_batch_del(table,toURI,"select[]",true,null);
	}
	/**
	 * 批量更新操作 2012-10-12
	 */
	public void m_batch_update(String table,String toURI,String select,MRecord inDatas,String msg,boolean tolist,Integer seg){
		String[] ids=MR.me().getParaValues(select);
		String where=null;
		for(String id:ids){
			where=MCnt.me().first("id", MEnum.EQ, id).toStr();
			M.me().update(table, inDatas, where);
		}
		seg=MCheck.isNull(seg)?2:seg;
		String path=tolist?getDefault_Redirect(toURI, "index"):getDefault_Redirect(toURI,"find/"+MURI.me().seg_str(seg)+"/"+MURI.me().seg_str(seg+1) );
		MCommHelp.outHTML(path,msg);
	}
	public void m_batch_update(String table,MRecord inDatas,String msg){
		m_batch_update(table,null,"select[]",inDatas,msg,true,2);
	}
	public void m_batch_update(String table,MRecord inDatas){
		m_batch_update(table,null,"select[]",inDatas,Msg.BATCH_MODIFY_SUCCESS,true,2);
	}
	public void m_batch_update(String table,String toURI,MRecord inDatas){
		m_batch_update(table,toURI,"select[]",inDatas,Msg.BATCH_MODIFY_SUCCESS,true,2);
	}
	public void m_batch_update(String table,MRecord inDatas,int seg){
		m_batch_update(table,null,"select[]",inDatas,Msg.BATCH_MODIFY_SUCCESS,true,seg);
	}	
}
