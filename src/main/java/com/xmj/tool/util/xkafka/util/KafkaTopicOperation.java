package com.xmj.tool.util.xkafka.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.security.JaasUtils;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;

public class KafkaTopicOperation {
	
	private ZkUtils zkUtils = null;
	public KafkaTopicOperation(String ipPort){
		this.zkUtils = ZkUtils.apply(ipPort, 30000, 30000, JaasUtils.isZkSecurityEnabled());
	}
	
	private void close(){
		if(zkUtils!=null){
			zkUtils.close();
		}
	}

	public void createKfkTopic(){
		// 创建一个单分区单副本名为t1的topic
		AdminUtils.createTopic(zkUtils, "t1", 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
		close();
	}
	
	public void deleteKfkTopic(){
		// 删除topic 't1'
		AdminUtils.deleteTopic(zkUtils, "t1");
		close();
	}
	
	public void selectKfkTopic(){
		// 获取topic 'test'的topic属性属性
		Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
		// 查询topic-level属性
		Iterator it = props.entrySet().iterator();
		while(it.hasNext()){
		    Map.Entry entry=(Map.Entry)it.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    System.out.println(key + " = " + value);
		}
		close();
	}
	
	public void changeKfkTopic(){
		Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
		// 增加topic级别属性
		props.put("min.cleanable.dirty.ratio", "0.3");
		// 删除topic级别属性
		props.remove("max.message.bytes");
		// 修改topic 'test'的属性
		AdminUtils.changeTopicConfig(zkUtils, "test", props);
		close();
	}
	
	
}
