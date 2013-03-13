package com.jzero.render;

import com.jzero.core.MInit;


/**
 * 2012-10-4:来源JFinal wangujqw@gmail.com
 */
public class MB{
	// singleton
	private MRender render;

	public MRender getRender() {
		return render;
	}
	public static MB me(){
		return MInit.get().getMb();
	}

	public MRender getJsonRender(String value) {
		render= new MJsonRender(value);
		return render;
	}

	public MRender getRender(String view) {
		render= new MJspRender(view);
		return render;
	}

	public MRender getJspRender(String view) {
		render= new MJspRender(view);
		return render;
	}
	public MRender getJspRender(){
		render=new MJspRender();
		return render;
	}
	public MRender getTextRender(String text) {
		render= new MTextRender(text);
		return render;
	}

	public MRender getTextRender(String text, String contentType) {
		render= new MTextRender(text, contentType);
		return render;
	}


	public MRender getRedirect301Render(String url) {
		render= new M301Render(url);
		return render;
	}

	public MRender getRedirect301Render(String url, boolean withOutQueryString) {
		render= new M301Render(url, withOutQueryString);
		return render;
	}

	public MRender getError404Render(String view) {
		render= new M404Render(view);
		return render;
	}

	public MRender getError404Render() {
		render= new M404Render();
		return render;
	}

	public MRender getError500Render(String view) {
		render= new M500Render(view);
		return render;
	}

	public MRender getError500Render() {
		render= new M500Render();
		return render;
	}
	public MRender getImgRender() {
		render= new MImaRender();
		return render;
	}
}
