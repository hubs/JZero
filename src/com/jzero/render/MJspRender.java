package com.jzero.render;

import com.jzero.log.Log;

/**
 * 2012-10-3: 来源于JFinal wangujqw@gmail.com
 */
class MJspRender extends MRender {

	private static final long serialVersionUID = 1L;


	public MJspRender() {
	}

	public MJspRender(String view) {
		setView(view);
	}

	public void render() {
		try {
			request.getRequestDispatcher(getView()).forward(request, response);
		} catch (Exception e) {
			Log.me().write_error(e);
		}
	}


	@Override
	public boolean isJsp() {
		// TODO Auto-generated method stub
		return true;
	}
}
