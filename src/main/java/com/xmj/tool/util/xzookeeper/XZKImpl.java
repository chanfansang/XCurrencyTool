package com.xmj.tool.util.xzookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.xmj.tool.util.xzookeeper.impl.XZKSelfWatcherImpl;
import com.xmj.tool.util.xzookeeper.util.SomeUtil;
import com.xmj.tool.util.xzookeeper.util.XZKUtil;
import com.xmj.tool.util.xzookeeper.util.XZKWatcher;
import com.xmj.tool.util.xzookeeper.vo.XZKVO;


public class XZKImpl implements XZKUtil{

	protected Log log = LogFactory.getFactory().getInstance(this.getClass());
	private ZooKeeper zookeeper;
	private CountDownLatch connectedSignal;

	private String serverList;

	// 读写锁，控制重启的时候使用
	private ReentrantReadWriteLock reentrantReadWriteLock;

	private Integer timeOut = 30 * 1000;

	
	public ZooKeeper getZookeeper() {
		return zookeeper;
	}
	
	/**
	 * 生成一个Zookeeper作为分布式管理的实现，没有观察接口
	 * 
	 * @param serverList
	 *            ZK集群服务器的地址集合，格式是IP:port,IP:port......
	 */
	public XZKImpl(String serverList) {
		this(serverList, null);
	}
	
	
	/**
	 * 生成一个Zookeeper作为分布式管理的实现，没有观察接口
	 * 
	 * @param serverList
	 *            ZK集群服务器的地址集合，格式是IP:port,IP:port......
	 * @param timeOut
	 *            zk超时的设定，默认为30S
	 */
	public XZKImpl(String serverList, Integer timeOut) {
		
		this.serverList = serverList;
		reentrantReadWriteLock = new ReentrantReadWriteLock();
		reentrantReadWriteLock.writeLock().lock();
		if (timeOut == null) {
			timeOut = 30 * 1000;
		} else {
			this.timeOut = timeOut;
		}
		try {
			zookeeper = getConnection(serverList);
		} catch (IOException e) {
			log.error("构造ZK连接集群服务器异常！serverList:" + serverList);
		} finally {
			reentrantReadWriteLock.writeLock().unlock();
		}

		try {
			//XZKFactory.initUtil(this);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}
	
	private ZooKeeper getConnection(final String serverList) throws IOException {
		connectedSignal = new CountDownLatch(1);
		ZooKeeper zkTmp = null;
		try {
			zkTmp = new ZooKeeper(serverList, timeOut, new Watcher(){
				@Override
				public void process(WatchedEvent event) {
					// TODO Auto-generated method stub
					if (event.getState() == KeeperState.SyncConnected) {
						connectedSignal.countDown(); // 倒数-1
					}else if (event.getState() == KeeperState.Expired) {
						log.error("zookeeper session expired! now reconnectiing");
			            try {
							close();
							getConnection(serverList);
						} catch (Exception e) {
							log.error("zookeeper connect error!",e);
						}
			        }
				}});
			//if(isDigest){
			//	zkTmp.addAuthInfo("digest", "admin:admin".getBytes());
			//}
			
			try {
				connectedSignal.await(timeOut * 2, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				log.error(e);
			}

		} catch (IOException e) {
			log.error("ZK连接集群服务器异常！serverList:" + serverList, e);
			throw e;
		}
		return zkTmp;
	}
	
	
	@Override
	public void initXData(XZKVO xzkvo) {
		// TODO Auto-generated method stub
		if(xzkvo!=null){
			reentrantReadWriteLock.readLock().lock();
			try{
				zookeeper.create(xzkvo.getPath(), xzkvo.getData(),
						xzkvo.getAcl(), xzkvo.getCreateMode());
			} catch (Exception e) {
				log.error("初始化异常！", e);
			} finally {
				reentrantReadWriteLock.readLock().unlock();
			}
		}
	}
	
	@Override
	public String register(XZKVO xzkvo) throws Exception {
		reentrantReadWriteLock.readLock().lock();
		String nodeName = null;
		try {
			if(xzkvo!=null){
				nodeName = xzkvo.getPath();
				for (int i = 0;; i++) {
					try {
						log.info("第" + i + "次尝试注册");
						
						zookeeper.create(nodeName,
									xzkvo.getData(),
									xzkvo.getAcl(), xzkvo.getCreateMode());
						
						// 放入ID
						Stat stat = zookeeper.exists(nodeName, false);
						xzkvo.setCzxid(stat.getCzxid());
						zookeeper.setData(nodeName,
								xzkvo.getData(), -1);
						// 注册监控自身节点变化
						XZKSelfWatcherImpl selfWatcher = new XZKSelfWatcherImpl(nodeName);
						registWatcher(selfWatcher);
						break;
					} catch (KeeperException.SessionExpiredException e) {
						log.error("session失效了，本节点需要重新尝试连接", e);
						throw new Exception(e);
					} catch (Exception e) {
						log.error("创建controller节点异常！", e);
						// 休眠五秒再做尝试
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			
		} finally {
			reentrantReadWriteLock.readLock().unlock();
		}
		return nodeName;
		
	}


	@Override
	public Object getNodeInfo(String path) {
		// TODO Auto-generated method stub
		reentrantReadWriteLock.readLock().lock();
		Object obj = null;
		try {
			byte[] tmpByteData = zookeeper.getData(path, false,
					new Stat());
			
			obj = SomeUtil.byteToJson(tmpByteData);
			
		} catch (Exception e) {
			log.error("获取跟节点数据异常！", e);
		} finally {
			reentrantReadWriteLock.readLock().unlock();
		}
		return obj;
	}

	@Override
	public void setNodeInfo(String path, Object obj) {
		// TODO Auto-generated method stub
		reentrantReadWriteLock.readLock().lock();
		try {
			zookeeper.setData(path, SomeUtil.getByte(obj),
					-1);
		} catch (Exception e) {
			log.error("更新跟节点数据异常！", e);
		} finally {
			reentrantReadWriteLock.readLock().unlock();
		}
	}

	@Override
	public boolean registWatcher(XZKWatcher watcher) {
		reentrantReadWriteLock.readLock().lock();
		try {
			if (watcher.isMatch(this)) {
				watcher.regWatcher(this);
				return true;
			} else {
				return false;
			}
		} finally {
			reentrantReadWriteLock.readLock().unlock();
		}
	}

	@Override
	public void removePath(String path) {
		reentrantReadWriteLock.readLock().lock();
		try {
			zookeeper.delete(path, -1);
		} catch (Exception e) {
			log.error("移除" + path + "异常！", e);
		} finally {
			reentrantReadWriteLock.readLock().unlock();
		}
	}

	@Override
	public boolean healthCheck(String path) {
		try {
			zookeeper.exists(path, false);
		} catch (KeeperException.SessionExpiredException e) {
			log.error("session失效了，本节点需要重新尝试连接", e);
			return false;
		} catch (KeeperException e) {
			log.error("健康判断异常！", e);
		} catch (InterruptedException e) {
			log.error("健康判断异常！", e);
		} catch (Exception e) {
			log.error("健康判断异常！", e);
		}
		return true;
	}

	@Override
	public void reconnection() {
		reentrantReadWriteLock.writeLock().lock();
		try {
			try {
				zookeeper.close();
			} catch (InterruptedException e1) {
				log.error("ZK关闭连接异常！", e1);
				e1.printStackTrace();
			}
			int i = 0;
			while (true) {
				try {
					zookeeper = null;
					zookeeper = getConnection(serverList);
					break;
				} catch (IOException e) {
					log.error("ZK第" + (++i) + "次重新连接失败！", e);
				}
			}
		} finally {
			reentrantReadWriteLock.writeLock().unlock();
		}
	}

	@Override
	public void close() throws Exception {
		try {
			zookeeper.close();
		} catch (InterruptedException e) {
			log.error("ZK关闭连接异常！", e);
			throw e;
		}
	}
	@Override
	public List<String> getList(String path) {
		List<String> extendList = null;
		reentrantReadWriteLock.readLock().lock();
		try {
			extendList = zookeeper.getChildren(path, false);
		} catch (Exception e) {
			log.error("获取所有异常！", e);
		} finally {
			reentrantReadWriteLock.readLock().unlock();
		}
		return extendList;
	}
	@Override
	public boolean isExit(String path) {
		reentrantReadWriteLock.readLock().lock();
		try {
			Stat stat = zookeeper.exists(path, false);
			if (stat == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			log.error("判断" + path + "是否存在异常！", e);
		} finally {
			reentrantReadWriteLock.readLock().unlock();
		}
		return false;
	}
	

	@Override
	public Integer getServerIdentity(String ip, Integer port) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getDownTime(String ip, Integer port) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
