package com.jzero.util;

import com.jzero.core.MR;
import com.jzero.core.MRouter;
import com.jzero.core.MURI;

public class MDividPage {
	private static int STEP = 3;// 偏移量,即显示2x3+1=7个
	private static int LEFT_NUM = 0;// 左界限
	private static int RIGHT_NUM = 0;// 右界限

	/**
	 * 
	 * @param currentPage
	 *            当前页数
	 * @param pageSize
	 *            每页显示数量
	 * @param totalSize
	 *            总记录数
	 * @return links
	 */
	public static String getPageLink(int currentPage, int pageSize,int totalSize, int step,Integer page_seg) {
		/** get totalPage **/
		STEP = step;

		/** get totalPage **/
		int totalPage = (totalSize % pageSize == 0) ? (totalSize / pageSize): (totalSize / pageSize + 1);
		String links = "";

		if (currentPage > totalPage)
			currentPage = totalPage;
		if (currentPage < 1)
			currentPage = 1;

		/** 总页数大于1时候才进行分页处理 **/
		if (totalPage > 1)
			links += MDividPage.setLink(currentPage, totalPage,
					totalSize,page_seg)+
					 MDividPage.setScript();

		return links;
	}

	/**
	 * 获取url链接
	 * localhost/pro/controller(0)/method(1)/order_str/find_str/page
	 * @return url
	 */
	public static String getUrl(Integer page_seg){
		String path=MTool.get_path();
		int level=MTool.get_level();
		int begin_seg=level==1?2:3;
		String method=MRouter.me().getMethod();
		if(MCheck.isNull(method)){ method="find";}
		
		String order=MTool.decode(MURI.me().seg_str(begin_seg));
		String find_str=MTool.decode(MURI.me().seg_str(begin_seg+1));
		order=MCheck.isNull(order)?MR.me().getAttrForStr(Msg.ORDER_STR):order;
		find_str=MCheck.isNull(find_str)?MR.me().getAttrForStr(Msg.FIND_STR):find_str;
		return MTool.getBase()+path+"/"+method+"/"+order+"/"+find_str;
	}
	public static String getUrl(){
		return getUrl(2);
	}

	/**
	 * 处理数字链接的左右边界值
	 * 
	 * @param currentPage
	 * @param totalPage
	 */
	public static void setBounds(int currentPage, int totalPage) {
		if (currentPage - STEP < 1) {
			LEFT_NUM = 1;
		} else {
			LEFT_NUM = currentPage - STEP;
		}
		if (currentPage + STEP > totalPage) {
			RIGHT_NUM = totalPage;
		} else {
			RIGHT_NUM = currentPage + STEP;
		}

		/** 如果页数大于(2xSTEP+1),但是显示少于(2xSTEP+1),则强制显示(2xSTEP+1) **/
		if (totalPage <= 2 * STEP + 1) {
			LEFT_NUM = 1;
			RIGHT_NUM = totalPage;
		} else {
			if (2 * STEP + 1 <= totalPage) {
				if (RIGHT_NUM < 2 * STEP + 1) {
					RIGHT_NUM = 2 * STEP + 1;
				}
			}
			if (totalPage - 2 * STEP > 0) {
				if (LEFT_NUM > totalPage - 2 * STEP) {
					LEFT_NUM = totalPage - 2 * STEP;
				}
			}
		}
	}

	public static String setLink(int currentPage, int totalPage, int totalSize,Integer page_seg) {

		/** 处理链接 **/
		String url = getUrl(page_seg);

		String links2 = "<ul id='pagination-digg'>";

		/** 显示分页信息 **/
		links2 += "<li title='Total Pages:" + totalPage + ",Current Page:"
				+ currentPage
				+ "' class='size'><a href='javascript:void(0)'>页码:"
				+ currentPage + "/" + totalPage + "</a></li>";

		/** 采用数字方式显示链接 **/

		/** 处理首页与上页 **/
		if (currentPage == 1) {
			links2 += "<li title='First Page' class='previous-off size'>&laquo;首页</li>";
			links2 += "<li title='Previous Page' class='previous-off size'>&#139;上页</li>";
		} else {
			links2 += "<li title='First Page' class='previous size'><a href='"
					+ url + "/1'>&laquo;首页</a></li>";
			links2 += "<li title='Previous Page' class='previous size'><a href='"
					+ url
					+ "/"
					+ (currentPage - 1)
					+ "'>&#139;上页</a></li>";
		}

		/** 获取左右边界值 **/
		setBounds(currentPage, totalPage);

		/** 处理中间页 **/
		for (int i = LEFT_NUM; i <= RIGHT_NUM; i++) {
			if (i != currentPage)
				links2 += "<li title='page " + i + "'><a href='" + url
						+ "/" + i + "'>" + i + "</a></li>";
			else
				links2 += "<li title='Current Page " + i + "' class='active'>"
						+ i + "</li>";
		}

		/** 处理下页与末页 **/
		if (currentPage == totalPage) {
			links2 += "<li title='Next Page' class='next-off size'>下页&#155;</li>";
			links2 += "<li title='Last Page' class='next-off size'>末页&raquo;</li>";
		} else {
			// links2+="<li title='Next Page' class='next size'><a href='"+url+"/"+(currentPage+1)+"'>下页&#155;</a></li>";
			// links2+="<li title='Last Page' class='next size'><a href='"+url+"/"+totalPage+"'>末页&raquo;</a></li>";
			links2 += "<li title='Next Page' class='next size'><a href='" + url+ "/" + (currentPage + 1) + "'>下页&#155;</a></li>";
			links2 += "<li title='Last Page' class='next size'><a href='" + url+ "/" + totalPage + "'>末页&raquo;</a></li>";
		}

		/** 添加跳转框 **/
	//	links2 += "<li class='page'><input type='text' id='ipage' class='ipage size2' title='请输入页码/Please Input a Page' value='"
	//			+ currentPage + "'/>";
	//	links2 += "<input title='总页数/Total Page-Read Only' type='text' id='itotal' class='itotal size2' value='/"
	//			+ totalPage + "' readonly='readonly'/></li>";
	
	//	links2 += "<li title='总记录数' class='go'><a href='javascript:void(0)'>总记录:("
	//			+ totalSize + ")条数据</a></li>";

		links2 += "</ul>";
		links2 += "<div style='clear:both;'></div>";
		return links2;
	}

	public static String setScript() {
		String script = "";

		return script;
	}

}
