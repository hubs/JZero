
package com.jzero.token;

import com.jzero.core.MInit;
import com.jzero.log.Log;
import com.jzero.util.MCheck;
import com.jzero.util.MCookie;
import com.jzero.util.MDate;
import com.jzero.util.MEncrypt;
import com.jzero.util.MPro;

/** 
 * 2012-10-6:防止从别的页面提交,参考于CI->Security.php
 * wangujqw@gmail.com
 */
public class MToken {

	public static MToken me(){ return MInit.get().getToken();}
	
	private String csrf_hash;							//加密后的值
	private int csrf_expire;						//存活时间(秒)
	private String csrf_toke_name="jzero_csrf_token";	//保存的名称
	private String csrf_cookie_name="jzero_csrf_token";	//保存在cookie中的名称
	private boolean protection;
	private boolean verify;
	
	
	public static MToken init(MInit minit){
		MToken token=new MToken();
		token.setCsrf_cookie_name(minit.getConfig().getStr("csrf_cookie_name"));
		token.setCsrf_toke_name(minit.getConfig().getStr("csrf_token_name"));
		token.setCsrf_expire(minit.getConfig().getInt("csrf_expire"));
		token.setProtection(minit.getConfig().getBool("csrf_protection"));
		token.setVerify(false);
		return token;
	}
	//验证
	public void csrf_verify(){
		if(!isVerify()){
			// Do the tokens exist in both the _POST and _COOKIE arrays?
			String token_name=MInit.get().getMr().getPara(getCsrf_toke_name());
			String cookie_name=MCookie.me().getCookie(getCsrf_cookie_name());
			if(MCheck.isNull(token_name)||MCheck.isNull(cookie_name)){
				csrf_show_error();
			}
			else if(!token_name.equals(cookie_name)){
				csrf_show_error();
			}else{
				MCookie.me().removeCookie(getCsrf_cookie_name());
				setCsrf_hash(null);
				csrf_set_hash();
				setVerify(true);
			}
		}
	}
	public void csrf_verify(Object object){
		if(!isVerify()){
			// Do the tokens exist in both the _POST and _COOKIE arrays?
			String cookie_name=MCookie.me().getCookie(getCsrf_cookie_name());
			if(MCheck.isNull(object)||MCheck.isNull(cookie_name)){
				csrf_show_error();
			}
			if(!object.equals(cookie_name)){
				csrf_show_error();
			}
			MCookie.me().removeCookie(getCsrf_cookie_name());
			setCsrf_hash(null);
			csrf_set_hash();
			setVerify(true);
		}		
	}
	private void csrf_show_error(){
		MInit.get().getMb().getTextRender("The action you have requested is not allowed.").render();
	}
	public String csrf_set_hash(){
		String csrf_hash=getCsrf_hash();
		if(MCheck.isNull(csrf_hash)){
			//从cookie中取,判断是否为空
			csrf_hash=MCookie.me().getCookie(getCsrf_cookie_name());
			if(MCheck.isNull(csrf_hash)){
				csrf_hash=MEncrypt.encrypt(MDate.get_ymd_hms());
			}else{
				setCsrf_hash(csrf_hash);
			}
			csrf_set_cookie(csrf_hash);
		}
		
		return csrf_hash;
	}
	public MToken csrf_set_cookie(String hash){
		int expire=MDate.get_now_second()+getCsrf_expire();
		MCookie.me().setCookie(getCsrf_cookie_name(), hash,expire,MPro.me().getStr("cookie_path"));
		Log.me().write_debug("CRSF cookie set");
		return this;
	}

	
	public String getCsrf_hash() {
		return csrf_hash;
	}
	

	public void setCsrf_hash(String csrfHash) {
		csrf_hash = csrfHash;
	}

	public int getCsrf_expire() {
		return csrf_expire;
	}

	public void setCsrf_expire(int csrfExpire) {
		csrf_expire = csrfExpire;
	}

	public String getCsrf_toke_name() {
		return csrf_toke_name;
	}

	public void setCsrf_toke_name(String csrfTokeName) {
		csrf_toke_name = csrfTokeName;
	}

	public String getCsrf_cookie_name() {
		return csrf_cookie_name;
	}

	public void setCsrf_cookie_name(String csrfCookieName) {
		csrf_cookie_name = csrfCookieName;
	}

	public boolean isProtection() {
		return protection;
	}

	public void setProtection(boolean protection) {
		this.protection = protection;
	}
	public boolean isVerify() {
		return verify;
	}
	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	
	
}
