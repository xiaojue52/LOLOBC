package com.wu.lolobc.comstant;

import java.util.Map;

public class WorkRunnable implements Runnable{

	private String url;
	private Map<String,String> pars;
	private CallBack callBack;

	public WorkRunnable(String url, Map<String, String> pars,CallBack callBack){
		this.url = url;
		this.pars = pars;
		this.callBack = callBack;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		callBack.downloadResult(Constant.getData(url, pars));
	}
}
