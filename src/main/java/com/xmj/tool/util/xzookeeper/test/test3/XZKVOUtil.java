package com.xmj.tool.util.xzookeeper.test.test3;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;

public interface XZKVOUtil {

	public Object get();
	public void set(Object obj);
	
	/*
	ZKEnvVar.ROOT, Czxid
	"base".getBytes(),
	Ids.CREATOR_ALL_ACL, 
	CreateMode.PERSISTENT
	*/
	public long getCzxid();
	public void setCzxid(long czxid);
	public String getPath();
	public void setPath(String path);
	public byte[] getData();
	public void setData(Object data) throws Exception;
	public List<ACL> getAcl();
	public void setAcl(List<ACL> acl);
	public CreateMode getCreateMode();
	public void setCreateMode(CreateMode createMode);
	
	
	
}
