package com.jzero.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jzero.core.MInit;
import com.jzero.core.MR;
import com.jzero.core.MRouter;
import com.jzero.log.Log;
import com.jzero.render.MB;
import com.jzero.render.MRender;
import com.jzero.util.MCheck;
import com.jzero.util.MPrint;
import com.jzero.util.MTool;

public class JFilter implements Filter{
//	private  String cachetime;
	public void init(FilterConfig filter) throws ServletException {
//		this.cachetime = filter.getInitParameter("cache");  
	}
	
	public void doFilter(ServletRequest req, ServletResponse resq,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resq;
		resq.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("UTF-8"); 
		resq.setCharacterEncoding("utf-8");
		MInit init=MInit.begin(request, response);
		String target=init.uri();
		boolean[] isHandled = { false };// 为什么用数组呢?因为如果用boolean,则不能返回值,如果用数据,则可以返回数组的值
		try {
			handle(request, response, target, isHandled);
		} catch (Exception e) {
			Log.me().write_error(e);
			MB.me().getJspRender(MRouter.me().getDefault_view());
//			init.getMb().getError500Render().setContext(request, response).render();
		}finally{
			if (isHandled[0] == false){
				if(target.lastIndexOf(".")!=-1){
					String extension =target.substring(target.lastIndexOf("."));//判断后辍名
					if(extension.equals(".asp")||extension.equals(".php")){
						if(target.contains("upload_json.jsp")){//kinditor上传时使用到 2012-11-21 extension.equals(".jsp")||
							chain.doFilter(request, response);
						}else{
							MPrint.print("试图运行>>"+target+",遭到拒绝.!");
						}
					}else{
						if(cache_path.contains(target)){
							response.setHeader("Cache-Control","max-age="+3600); //HTTP 1.1
						}else{
							response.setHeader("Cache-Control","no-cache");
						    response.setHeader("Pragma","no-cache"); //HTTP 1.0
						    response.setDateHeader ("Expires", 0 );
						}
						chain.doFilter(request, response);
					}
				}
			}
			if(!MCheck.isNull(init)){
				init.end();
			}
		}
		
	}
	//放行的后辍名
	private final static String[] static_ext ={
		"csv","js","css","jpg","png","gif",".html","ico","swf","mp4","com","doc","xls","pdf","ppt","gz","gtar","rar","tar","tgz","zip","mp3","jpeg","jpe","tif","html","txt","jsp","rm","rmvb","flv","mpeg","mov","mtv","avi","3gp","amv","dmv"
	};
	//放行的包名
	private final static String[] static_packer={
		"upload","js","css","images","swf","other"
	};
	private static List<String> allow=Arrays.asList(static_ext);
	private static List<String> allow_packer=Arrays.asList(static_packer);
	
	private static List<String> cache_path=new ArrayList<String>(500);
	private boolean checkCachePath(String target){
		return cache_path.contains(target)?true:false;
	}
	public final void handle(HttpServletRequest request,
			HttpServletResponse response, String target, boolean[] isHandled){
		if(checkCachePath(target)){//如果缓存中已经包含,则直接调用
			return;
		}else{
			if (target.indexOf(".") != -1) {// 如果包含有.的一律放行,像.js,.css,.jpg// 2012-10-8,来源于JFinal
				String extension =target.substring(target.lastIndexOf(".")+1);//判断后辍名
				if(allow.contains(extension)||extension.contains("com")){//如果包含指定的项目,是放行,eg http://glchy.gotoip1.com/
					cache_path.add(target);
					return ;
				}
			}
			String projectName=MR.me().getProject();
			String target_path=MCheck.isNull(projectName)?target:target.replaceFirst(projectName, "");// /xxx/xx
			target_path=target_path.replaceFirst("/", "");
			String packags=MCheck.isNull(target_path)?"":target_path.contains("/")?target_path.substring(0, target_path.indexOf("/")):target_path;
			//过滤了指定的文件包名:2012-11-23
			if(allow_packer.contains(packags)){//以斜线开头
				cache_path.add(target);
				return;
			}
			isHandled[0] = true;
			MInit mInit =MInit.get();
			String[] uri_s=mInit.getURI().parse(target);
			mInit.getRouter().run(uri_s);//路由器调用
			MRender render =mInit.getMb().getRender();//返回的界面
			if (MCheck.isNull(render.getView()) && render.isJsp()) {
				String viewPath = MTool.get_path()+"/"+ mInit.getRouter().getMethod() + ".jsp";
				render.setContext(request, response,viewPath).render();
			} else {
				render.setContext(request, response).render();
			}
		}
	}

	public void destroy() {
	}
}
