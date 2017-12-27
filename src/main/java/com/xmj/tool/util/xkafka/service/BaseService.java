package com.xmj.tool.util.xkafka.service;

import org.springframework.beans.factory.DisposableBean;

public abstract class BaseService implements DisposableBean,Runnable{
	
	private int count = 0;
	public boolean isRun = false;
	public BaseService(){
		this.isRun = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	abstract public void run();
	
	@Override
	public void destroy() throws Exception {
		this.isRun = false;
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
