package com.xmj.tool.util.xzookeeper.test.test3;

import com.xmj.tool.util.xzookeeper.test.test3.impl.XZKVO;

public interface XZKUtil {

	public void initXData(XZKVO xzkvo);
	public Object getNodeInfo(String path);
	public void setNodeInfo(String path, Object obj);
	
	public String register(XZKVO xzkvo) throws Exception;
	
	/**
	 * 
	 * registWatcher 注册一个监听
	 * 
	 * @param watcher
	 *            接口的实现
	 * @return
	 */
	public boolean registWatcher(XZKWatcher watcher);
	/**
	 * 
	 * removePath 移除指定路径的节点
	 * 
	 * @param path
	 *            需要移除节点的全路径
	 */
	public void removePath(String path);
	/**
	 * 
	 * healthCheck 健康检查
	 * 
	 * @param 健康返回真
	 *            ，否则返回假
	 */
	public boolean healthCheck(String path);

	/**
	 * 
	 * reconnection 重新连接包括关闭和重新连接
	 */
	public void reconnection();

	public void close() throws Exception;

	/**
	 * 
	 * getServerIdentity 获取server的身份
	 * 
	 * @param ip
	 *            IP串，不一定为单个IP，一个机器的IP总和
	 * @param port
	 *            端口，这个server的端口
	 * @return DCFactory.CONTROLLER或者DCFactory.EXECUTOR， 如果返回为null的话证明没有使用身份判断
	 */
	public Integer getServerIdentity(String ip, Integer port) throws Exception;

	/**
	 * 
	 * getDownTime 获取机器宕机时间 单位为毫秒
	 * 
	 * @param ip
	 *            IP串，不一定为单个IP，一个机器的IP总和
	 * @param port
	 *            端口，这个server的端口
	 * @return 获取机器宕机时间，默认为十分钟
	 */
	public Long getDownTime(String ip, Integer port) throws Exception;
	
	
	
	
}
