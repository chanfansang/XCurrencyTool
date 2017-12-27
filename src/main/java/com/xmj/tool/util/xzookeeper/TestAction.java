package com.xmj.tool.util.xzookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

import com.xmj.tool.util.xzookeeper.impl.XZKSelfWatcherImpl;
import com.xmj.tool.util.xzookeeper.util.XZKUtil;
import com.xmj.tool.util.xzookeeper.util.XZKWatcher;
import com.xmj.tool.util.xzookeeper.vo.XZKVO;

public class TestAction {

	
	public static void main(String[] args) throws Exception {
		String serverList = "10.31.2.23";
		/** 
         * 此处采用的是CreateMode是PERSISTENT  表示The znode will not be automatically deleted upon client's disconnect. 
         * EPHEMERAL 表示The znode will be deleted upon the client's disconnect. 
         */ 
		XZKImpl xzk = new XZKImpl(serverList);
		//xzk.close();
		//System.out.println("已关闭");
		//Thread.sleep(2000);
		//initXData(xzk);
		System.out.println("1:"+xzk.getNodeInfo("/root1"));
		//Thread.sleep(2000);
		//register(xzk);		
		System.out.println("2:"+xzk.getNodeInfo("/root1/test1"));
		//Thread.sleep(2000);
		//XZKSelfWatcherImpl watcher = new XZKSelfWatcherImpl("/root1/test1");
		//System.out.println("3:"+xzk.registWatcher(watcher));
		System.out.println("4:"+xzk.healthCheck("/root1"));
		//Thread.sleep(2000);
		//register2(xzk);
		System.out.println("5:"+xzk.getNodeInfo("/root1/test2"));
		//Thread.sleep(2000);
		//xzk.removePath("/root1/test1");
		System.out.println("6:"+xzk.getNodeInfo("/root1/test1"));
		//Thread.sleep(2000);
		xzk.setNodeInfo("/root1/test2", "{\"a4\":\"b5\"}");
		System.out.println("5:"+xzk.getNodeInfo("/root1/test2"));
		//Thread.sleep(2000);
		xzk.reconnection();
		System.out.println("8:"+xzk.getNodeInfo("/root1"));
		System.out.println("9:"+xzk.getNodeInfo("/root1/test1"));
		System.out.println("10:"+xzk.getList("/root1"));
		System.out.println("11:"+xzk.isExit("/root1/test1")+","+xzk.isExit("/root1/test3"));
	}
	
	public static void initXData(XZKImpl xzk){
		XZKVO xzkvo1 = new XZKVO();
		try {
			xzkvo1.setAcl(Ids.OPEN_ACL_UNSAFE);
			xzkvo1.setCreateMode(CreateMode.PERSISTENT);
			xzkvo1.setData("{\"a\":\"b\"}");
			xzkvo1.setPath("/root1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xzk.initXData(xzkvo1);
	}
	
	public static void register(XZKImpl xzk) throws Exception{
		XZKVO xzkvo1 = new XZKVO();
		try {
			xzkvo1.setAcl(Ids.OPEN_ACL_UNSAFE);
			xzkvo1.setCreateMode(CreateMode.PERSISTENT);
			xzkvo1.setData("{\"a1\":\"b1\"}");
			xzkvo1.setPath("/root1/test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xzk.register(xzkvo1);
	}
	public static void register2(XZKImpl xzk) throws Exception{
		XZKVO xzkvo1 = new XZKVO();
		try {
			xzkvo1.setAcl(Ids.OPEN_ACL_UNSAFE);
			xzkvo1.setCreateMode(CreateMode.PERSISTENT);
			xzkvo1.setData("{\"a2\":\"b2\"}");
			xzkvo1.setPath("/root1/test2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xzk.register(xzkvo1);
	}
}
