package com.xmj.tool.util.xkafka.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmj.tool.util.xkafka.util.KafkaProducer_1_0_apache;

public class DataSendToKafkaService extends BaseService {

	private static Logger log = LoggerFactory.getLogger(DataSendToKafkaService.class);

	private KafkaProducer_1_0_apache producer = null;

	private BlockingQueue<Object> sendMsgQ = new LinkedBlockingQueue<Object>(100000);

	private String zookeeper_connect = null;
	private String topic = null;
	
	/** 
     * 添加信息至队列中 
     * @param content 
     */  
    public void addQueue(Object content) { 
    	if(content == null||content.equals(null)){
    		sendMsgQ.offer(content);
    	}  	  
    }  

	public DataSendToKafkaService(String zookeeper_connect, String topic) {
		super();
		this.zookeeper_connect = zookeeper_connect;
		this.topic = topic;
	}

	@Override
	public void run() {

		while (isRun) {
			boolean isSleep = true;
			try{
				Object sendMsg = sendMsgQ.poll(1, TimeUnit.MILLISECONDS);
				if (sendMsg != null) {
					sendObject(sendMsg);
					isSleep = false;
				}	
				
				if (isSleep) {
					Thread.sleep(1000L);
				}
			} catch (Exception e) {
				log.error("",e);
			} 
			 
		}
	}

	
	public void sendObject(Object o) {
		byte[] b = null;		
		b = (byte[]) o;		
		try {
			
			if(null != producer && !producer.isNormal()){
				producer.close();
				producer = null;
			}
			if (null == producer) {
				producer = new KafkaProducer_1_0_apache(log);
				Properties props = new Properties();
				props.put("zookeeper.connect", zookeeper_connect);
				producer.init(props);
			}
			producer.send(null, b, topic, null, null, false);
		} catch (Exception e) {
			log.error("send kafka Exception fail,", e);
			
			if (null != producer) {
				producer.close();
				producer = null;
			}
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}catch (Throwable e) {
			log.error("send kafka Throwable fail,", e);
			
			if (null != producer) {
				producer.close();
				producer = null;
			}
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	private byte[] toByteArray (Object obj) {     
        byte[] bytes = null;     
        ByteArrayOutputStream bos = new ByteArrayOutputStream();     
        try {       
            ObjectOutputStream oos = new ObjectOutputStream(bos);        
            oos.writeObject(obj);       
            oos.flush();        
            bytes = bos.toByteArray ();     
            oos.close();        
            bos.close();       
        } catch (IOException ex) {       
            ex.printStackTrace();  
        }     
        return bytes;   
    }  

}
