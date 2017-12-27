package com.xmj.tool.util.xzookeeper.test.test3.impl;

import com.xmj.tool.util.xzookeeper.test.test3.XZKUtil;

public class XZKFactory {

	private static XZKUtil xzkUtil;

	private static String nodeName;

	private static Integer identity;

	public static void initUtil(XZKUtil xzkUtil) throws Exception {
		if (XZKFactory.xzkUtil != null) {
			throw new Exception("初始化方法重复调用！请检查");
		}
		XZKFactory.xzkUtil = xzkUtil;
	}

	public static void initNodeName(String nodeName, Integer identity) {
		XZKFactory.nodeName = nodeName;
		XZKFactory.identity = identity;
	}

	public static void reSet(String nodeName, Integer identity)
			throws Exception {
		if (xzkUtil == null) {
			throw new Exception("没有初始化，请先初始化后才可重置");
		}
		initNodeName(nodeName, identity);
	}

	public static XZKUtil getXzkUtil() throws Exception {
		if (xzkUtil == null) {
			throw new Exception("分布式工具没有初始化！请初始化后使用！初始化方法：调用initUtil");
		}
		return xzkUtil;
	}

	public static String getNodeName() throws Exception {
		if (nodeName == null) {
			throw new Exception("分布式工具没有初始化！请初始化后使用！初始化方法：调用initUtil");
		}
		return nodeName;
	}

	public static Integer getIdentity() throws Exception {
		if (identity == null) {
			throw new Exception("分布式工具没有初始化！请初始化后使用！初始化方法：调用initUtil");
		}
		return identity;
	}

	public static boolean isIdIdentityChange(Integer identity) {
		if (XZKFactory.identity == null) {
			return true;
		}
		if (XZKFactory.identity.equals(identity)) {
			return false;
		} else {
			return true;
		}
	}
	
}
