package com.xmj.tool.util.xzookeeper.test.test3.impl;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;

import com.alibaba.fastjson.JSON;
import com.xmj.tool.util.xzookeeper.test.test3.XZKVOUtil;

public class XZKVO implements XZKVOUtil{

	private long czxid;
	private String path;
	private byte[] data;
	private List<ACL> acl;
	private CreateMode createMode;
	private Object obj;
	
	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return obj;
	}

	@Override
	public void set(Object obj) {
		// TODO Auto-generated method stub
		this.obj = obj;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	@Override
	public void setPath(String path) {
		// TODO Auto-generated method stub
		this.path = path;
	}

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void setData(Object objData) throws Exception {
		// TODO Auto-generated method stub
		String json = JSON.toJSONString(objData);
		this.data = json.getBytes("UTF-8");
		
	}

	@Override
	public List<ACL> getAcl() {
		// TODO Auto-generated method stub
		return acl;
	}

	@Override
	public void setAcl(List<ACL> acl) {
		// TODO Auto-generated method stub
		this.acl = acl;
	}

	@Override
	public CreateMode getCreateMode() {
		// TODO Auto-generated method stub
		return createMode;
	}

	@Override
	public void setCreateMode(CreateMode createMode) {
		// TODO Auto-generated method stub
		this.createMode = createMode;
	}

	@Override
	public long getCzxid() {
		// TODO Auto-generated method stub
		return czxid;
	}

	@Override
	public void setCzxid(long czxid) {
		// TODO Auto-generated method stub
		this.czxid = czxid;
	}

}
