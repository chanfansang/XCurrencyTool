package com.xmj.tool.util.xkafka.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.xmj.tool.util.xkafka.util.KafkaConsumer_1_0_apache;
//@Component
public class DataLoadFromKafkaService extends BaseService{
	
	private static Logger log =LoggerFactory.getLogger(DataLoadFromKafkaService.class);
	public boolean isRun = false;
	private KafkaConsumer_1_0_apache consumer = null;
	private String zookeeper_connect = null;
	private String topic = null;
	private BlockingQueue<Object> loadMsgQ = new LinkedBlockingQueue<Object>(100000);
	
	public DataLoadFromKafkaService(String zookeeper_connect, String topic){
		super();
		this.zookeeper_connect = zookeeper_connect;
		this.topic = topic;
	}
	
	/** 
     * 添加信息至队列中 
     * @param content 
     */  
    public void addQueue(Object content) { 
    	if(content == null||content.equals(null)){
    		loadMsgQ.offer(content);
    	}  	  
    }  
    
    public Object getQueue(){
    	return loadMsgQ.poll();
    }
	
	@Override
	public void run(){
		
		while (isRun) {
			List<ConsumerRecord<Object, Object>> list = new ArrayList<ConsumerRecord<Object, Object>>();
			try {
				receiveObject(list);
			} catch (Exception e) {
				log.error("",e);
				try {
					consumer.close();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				consumer = null;
			}
			boolean isPropertyChange = false;
			if(list.size() > 0){
				for(ConsumerRecord<Object, Object> record : list){
					String msg = (String)(record.value());
					log.info("receive one record offset[" + record.offset() + "],partition[" + record.partition() + "],topic[" + record.topic() + "],msg[" + msg + "]");
					JSONObject webMsg =  (JSONObject) JSONObject.parse(msg);
					
					addQueue(webMsg);
					
				}
				list.clear();
			}else{
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		
	}
	
	private void receiveObject(List<ConsumerRecord<Object, Object>> list) throws Exception{
		if(null == consumer){
			consumer = new KafkaConsumer_1_0_apache(log);
			Properties props = new Properties();
			props.put("zookeeper.connect", zookeeper_connect);
			consumer.init(props);
			consumer.subscribe(topic, null);
		}
		
		consumer.receive(list);
	}
	
}
