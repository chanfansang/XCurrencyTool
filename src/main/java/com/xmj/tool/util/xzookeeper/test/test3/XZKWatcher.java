package com.xmj.tool.util.xzookeeper.test.test3;

public interface XZKWatcher {
	/**
	 * 
	 * isMatch 判断watcher和XZK的实现是否一致
	 * 
	 * @param xZKUtil
	 *            XZK的接口
	 * @return 一致返回true不一致返回false
	 */
	public boolean isMatch(XZKUtil xZKUtil);

	/**
	 * 
	 * regWatcher
	 * 
	 * @param xZKUtil
	 *            XZK的接口
	 */
	public void regWatcher(XZKUtil xZKUtil);
}
