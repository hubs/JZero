package com.gl.action;

import java.util.List;

import com.jzero.db.core.M;
import com.jzero.util.MCheck;
import com.jzero.util.MPrint;
import com.jzero.util.MRecord;

public class Welcome {

	public void index(){
		List<MRecord> lst=M.me().get_table("xt_category");
		if(!MCheck.isNull(lst)){
			for(MRecord row:lst){
				MPrint.print(row);
			}
		}
	}
}
