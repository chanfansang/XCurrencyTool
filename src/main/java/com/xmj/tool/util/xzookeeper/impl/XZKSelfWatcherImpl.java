package com.xmj.tool.util.xzookeeper.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.xmj.tool.util.xzookeeper.XZKImpl;
import com.xmj.tool.util.xzookeeper.util.XZKUtil;
import com.xmj.tool.util.xzookeeper.util.XZKWatcher;
import com.xmj.tool.util.xzookeeper.vo.XZKFactory;


public class XZKSelfWatcherImpl implements Watcher, XZKWatcher {
	protected Log log = LogFactory.getFactory().getInstance(this.getClass());
	

	// 节点路径
	private String nodePath;

	// 判断是否需要重新注册
	private boolean isNeedReg = true;

	public XZKSelfWatcherImpl(String nodePath) {
		super();
		this.nodePath = nodePath;
		
	}

	public void process(WatchedEvent event) {
		XZKUtil xzkUtil = null;
		String nodeName;
		try {
			xzkUtil = XZKFactory.getXzkUtil();
			nodeName = XZKFactory.getNodeName();
		} catch (Exception e) {
			log.error("dc未初始化", e);
			return;
		}
		if (event.getType().equals(EventType.NodeDataChanged)) {
			Integer port = 0;
			String serverName = null;
			String specifyIp = null;
			

		}

		if (isNeedReg) {
			regWatcher(xzkUtil);
		}
	}


	@Override
	public boolean isMatch(XZKUtil xZKUtil) {
		if (xZKUtil instanceof XZKImpl) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void regWatcher(XZKUtil xZKUtil) {
		if (!isMatch(xZKUtil)) {
			log.error("注册监听和分布式管理类型不一致，监听为Zookeeper的监听实现");
			return;
		}
		XZKImpl impl = (XZKImpl) xZKUtil;
		ZooKeeper zooKeeper = impl.getZookeeper();
		try {
			zooKeeper.getData(nodePath, this, new Stat());
		} catch (Exception e) {
			log.error("注册监听异常！", e);
		}
		
	}

}

