JZero 
=====

JZero=Codeigniter+JFinal 

一个简单的Java Servlet框架,框架的主要思想来源于 Codeigniter与JFinal二大框架,目前自已使用于公司的项目中.

JZero会根据当前项目中出现的问题进一步完善.


package com.gl.action.backstage;

import com.gl.utils.MMsg;
import com.jzero.comm.MComm;
import com.jzero.core.MR;
import com.jzero.util.MDate;
import com.jzero.util.MRecord;
import com.jzero.util.MSession;

/** 
 * 2012-11-15: 友情链接
 * wangujqw@gmail.com
 */
public class Friend {

  public void index(){
		MComm.me().m_list(MMsg.WZ_FRIEND, new Object[]{"order by lrry desc"});
	}
	public void find(){
		MComm.me().m_find(MMsg.WZ_FRIEND, new Object[]{"order by lrry desc"});
	}
	public void add(){
		MComm.me().m_add();
	}
	public void save(){
		MRecord in=MR.me().getPara();
		in.set("lrrq", MDate.get_ymd_hms()).set("lrry", MSession.getName());
		MComm.me().m_save(MMsg.WZ_FRIEND,in);
	}
	public void edit(){
		MComm.me().m_edit(MMsg.WZ_FRIEND,3);
	}
	public void update(){
		MComm.me().m_update(MMsg.WZ_FRIEND);
	}
	public void del(){
		MComm.me().m_del(MMsg.WZ_FRIEND,3);
	}
	public void view(){
		MComm.me().m_view(MMsg.WZ_FRIEND,3);
	}
	public void batch(){
		MComm.me().m_batch_del(MMsg.WZ_FRIEND);
	}
}

