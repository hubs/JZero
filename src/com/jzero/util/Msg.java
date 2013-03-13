
package com.jzero.util;
/** 2012-10-3 */
public class Msg {
	//--配置文件 
	public static String CONFIG					=	"config.properties";
	public static String DB_CONFIG				=	"database.properties";
	public static String ROUTE_CONFIG			=	"routes.properties";
	public static String OTHER_CONFIG			=	"other.properties";
	
	//--传出到页面的指定值
	public static String OUT_DATAS				=	"OUT_DATAS";   //输出到新增或修改页面的值
	public static String LIST_DATAS				=	"LIST_DATAS";  //输出到新增或修改的列表值
	public static String OBJECT					=	"OBJECT";	   //输出到修改页面的对象值
	public static String ORDER_STR				=	"ORDER_STR";   //隐藏字段,排序名称
	public static String FIND_STR				=	"FIND_STR";	   //隐藏字段,查询名称
	public static String SELECT_DATAS			=	"SELECT_DATAS";//检索处,共同的下拉框数据
	public static String SHOW_COMM				=	"SHOW_COMM";   //是否显示共同检索信息
	public static String SHOW_TIME				=	"SHOW_TIME";   //是否显示时间检索信息
	public static String SHOW_KIND				=	"SHOW_KIND";
	public static String SHOW_ADD				=	"SHOW_ADD";    //是否显示增加按键
	public static String SHOW_BACK				=	"SHOW_BACK";	//是否显示返回按钮
	public static String ADD_URI				=	"ADD_URI";		//增加地址信息
	public static String SAVE_URI				=	"SAVE_URI";		//更新的地址信息
	public static String FIND_URI				=	"FIND_URI";		//查询地址信息
	
	//--提示信息
	public static String MESSAGE				=	"MESSAGE";	   //提示信息
	
	//--修改
	public static String MODIFY_SUCCESS			=	"更新成功.";
	public static String MODIFY_ERROR			=	"更新失败,请检查是否已修改属性值。";
	public static String BATCH_MODIFY_SUCCESS	=	"批量操作成功.";

	//--审核
	public static String AUDIT_SUCCESS			=	"审核成功.";
	public static String AUDIT_ERROR			=	"审核失败.";
	public static String BATCH_AUDIT_SUCCESS	=	"批量审核成功.";
	
	//--删除
	public static String DELETE_SUCCESS			=	"删除成功.";
	public static String BATCH_DELETE_SUCCESS	=	"批量删除成功.";
	public static String DELETE_ERROR			=	"删除时发生错误.请清理一下缓存.";
	
	//--新增
	public static String INSERT_SUCCESS			=	"新增成功";
	public static String INSERT_ERROR			=	"新增时发生错误,请清理缓存再试一次.";
	
	//--保存
	public static String SAVE_SUCCESS			=	"操作成功.";
	
	
	//--登录信息
	public static String USER_PASS_ERROR		=	"用户名或密码错误,请重新输入";
	public static String USER_IS_STOP			=	"当前用户状态已停用,已禁止登录,请联系管理员.";
	

	
	public static String USER_NAME				=	"super_user";
	/**
	 * 验证码为空
	 */
	public static final String AUTH_NULL 		= 	"验证码是空值,请输入验证码.";
	/**
	 * 验证码错误
	 */
	public static final String AUTH_ERROR 		= 	"验证码错误,请重新输入.";
	
	
	//常量信息
	public static final String CON_FILE			=	"filename";		//文件上传时使用到　　2012-11-3
	public static final String CON_IMG			=	"imgname";		//图片上传时使用到　　2012-11-3
	public static final String CON_MUL_IMG		=	"mulimg";		//多上传时使用到　　2012-11-3
}
