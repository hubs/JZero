package com.jzero.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.jzero.aop.Uncheck;
import com.jzero.log.Log;
import com.jzero.render.MB;
import com.jzero.util.MCheck;
import com.jzero.util.MSession;
import com.jzero.util.MTool;

/**
 * 2012-10-3: 参考CI Router ,路由功能
 * wangujqw@gmail.com
 */
@SuppressWarnings("unchecked")
public class MRouter{

	private String controller;
	private String method;
	private String directory;
	private String default_controller;
	private String default_method;
	private String controller_package;
	private String default_view;
	/**
	 * 后面操作包名,用于区别前台操作,如果此时的后台SESSION为空,则跳出到登录页面
	 * 2012-11-5
	 */
	private String backstage_package;
	private final static String DOT=".";
	private boolean dev;

	public static MRouter me(){return MInit.get().getRouter();}

	public static MRouter init(MInit init){
		MRouter router=new MRouter();
		router.setDefault_controller(init.getConfig().getStr("default_controller"));
		router.setDefault_method(init.getConfig().getStr("default_method"));
		router.setController_package(init.getConfig().getStr("controller_package"));
		router.setDev(init.getConfig().getBool("is_dev"));
		router.setBackstage_package(init.getConfig().getStr("backstage_package"));
		router.setDefault_view(init.getConfig().getStr("default_view"));
		return router;
	}

	public void run(String[] uri_s){
		if(MCheck.isNull(uri_s)){
			invoke_default();
		}else{
			invoke(uri_s);
		}
	}
	
	
	private void invoke_default() {
		String path=getController_package()+DOT+getDefault_controller();
		Class c=null;
		try {
			c=Class.forName(path);
			setController(getDefault_controller());
		} catch (ClassNotFoundException e) {
			Log.me().write_error(e);
			MReport.doErrorReport(path, getDefault_controller(), getDefault_method());
			MInit.get().getMb().getTextRender("未找到["+path+"] ..").render();
		}
		if(!MCheck.isNull(c)){
			try {
				if(MCheck.isNull(getDefault_method())){
					setDefault_method("index");
				}
				Method m = c.getDeclaredMethod(getDefault_method());
				Object obj = c.newInstance();
				m.invoke(obj);
				setMethod(getDefault_method());
				if(isDev()){
					MReport.doReport(path, getDefault_controller(), getDefault_method());
				}
				
			}catch (Exception e) {
				e.printStackTrace();
				Log.me().write_error(e);
				if(isDev()){
					MReport.doErrorReport(path, getDefault_controller(), getDefault_method());
				}
				MInit.get().getMb().getTextRender("执行["+getDefault_controller()+"/"+getDefault_controller()+"] 出现错误").render();
			}
		}
	}

	/**
	 * 验证是否存在,只支持有二级目录
	 * eg:一级目录为:com.gl.test
	 * 	     二级目录为:com.gl.test.back
	 * @param mInit 
	 */
	
	private void invoke(String[] uris) {
		setController(MTool.firstCharToUpperCase(MURI.me().seg_str(0, uris)));//默认控制器路径 uris[0]
		String path=getController_package()+DOT+getController();//控制器名称
		Class c=null;
		try {
			
			c=Class.forName(path);
			setDirectory(MURI.me().seg_str(0, uris));
			setMethod(MURI.me().seg_str(1, uris));
		} catch (ClassNotFoundException e) {
			setController(MTool.firstCharToUpperCase(MURI.me().seg_str(1, uris)));//查询子页面中的控制器
			path=path.toLowerCase()+DOT+getController();
			try {
				c=Class.forName(path);
				setDirectory(MURI.me().seg_str(0, uris));
				setMethod(MURI.me().seg_str(2, uris));
			} catch (ClassNotFoundException e1) {
				Log.me().write_error(e1);
				MReport.doErrorReport(path, getController(), getMethod(), uris);
				MInit.get().getMb().getTextRender("未找到["+path+"] ...").render();
			}
		}
		
		//在这里判断当前SESSION是否为空
			if(!MCheck.isNull(c)){
				boolean check_session=check_current_session_is_null(c);
				if(check_session){
					try {
						if(MCheck.isNull(getMethod())){
							setMethod("index");
						}
						Method m = getMeCls(getController(), getMethod(), c);
						Object obj=getClsName(getController(), c);
						m.invoke(obj);	
						if(isDev()){
							MReport.doReport(path, getController(), getMethod(), uris);
						}
					}catch (Exception e) {
						Log.me().write_error(e);
//						MB.me().getJspRender(getDefault_view());
						MInit.get().getMb().getTextRender("未找到["+path+"."+getMethod()+"]").render();
						if(isDev()){
							MReport.doErrorReport(path, getController(), getMethod(), uris);
						}
					}
				}	
			}
	}
	//2012-11-13
	private boolean check_current_session_is_null(Class cls){
		String dir=getDirectory();
		if(getBackstage_package().contains(dir)){
			//在这检测是否有NO_SESSION　标签
			Uncheck uncheck=(Uncheck) cls.getAnnotation(Uncheck.class);
			if(MCheck.isNull(uncheck)){//等于空,则说需要检测SESSION值
				if(MCheck.isNull(MSession.get())){
					MB.me().getJspRender(getDefault_view());
					return false;
				}
			}
		}
		return true;
	}
	private static Map<String,Class> cls=new HashMap<String,Class>();//存放类
	private static Map<String,Method> mcls=new HashMap<String, Method>();//存放方法
	private Object getClsName(String name,Class clsname) throws Exception{
		if(cls.containsKey(name)){
			return cls.get(name).newInstance();
		}
		cls.put(name, clsname);
		return clsname.newInstance();
	}
	private Method getMeCls(String controller,String method,Class c) throws Exception{
		String key=controller+method;
		if(mcls.containsKey(key)){
			return mcls.get(key);
		}
		Class cls_name;
		if(cls.containsKey(controller)){
			cls_name=cls.get(controller);//从类容器中取出类名
		}else{
			cls_name=c;
		}
		Method m =cls_name.getDeclaredMethod(method);
		mcls.put(key, m);
		return m;	
	}
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDefault_controller() {
		return default_controller;
	}

	public void setDefault_controller(String defaultController) {
		default_controller = defaultController;
	}

	public String getDefault_method() {
		return default_method;
	}

	public void setDefault_method(String defaultMethod) {
		default_method = defaultMethod;
	}

	public String getController_package() {
		return controller_package;
	}

	public void setController_package(String controllerPackage) {
		controller_package = controllerPackage;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}


	public boolean isDev() {
		return dev;
	}


	public void setDev(boolean dev) {
		this.dev = dev;
	}


	public String getBackstage_package() {
		return backstage_package;
	}


	public void setBackstage_package(String backstagePackage) {
		backstage_package = backstagePackage;
	}

	public String getDefault_view() {
		return default_view;
	}

	public void setDefault_view(String defaultView) {
		default_view = defaultView;
	}
	
}
