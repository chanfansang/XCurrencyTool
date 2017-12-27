package com.xmj.tool.util.xkafka.util;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;


/*
 * 建议参数
 * 1、acks=1
 * 2、retries=1
 * 3、batch.size=1024*256/1024*128(128k/256k)
 * 4、linger.ms=1000(1秒)
 */
public class KafkaProducer_1_0_apache {
	
	private  org.apache.kafka.clients.producer.Producer<Object, Object> producer = null;
	private Logger log = null;
	private boolean status = true;
	public KafkaProducer_1_0_apache(Logger log){
		this.log = log;
	}
	
	public void send(Object key,Object value,String topic,Integer msg_type,Integer partitionId,boolean isTimeStamp){
		ProducerRecord<Object, Object> record = null;
		if(null == key){
			record =  new ProducerRecord<Object, Object>(topic,value);
		}else if(null == partitionId){
			record = new ProducerRecord<Object, Object>(topic,key,value);
		}else{
			record = new ProducerRecord<Object, Object>(topic,partitionId,key,value);
		}
		
		this.producer.send(record, new KafkaSendCallback(record,this));
	}
	
	public void flush(){
		this.producer.flush();
	}
	
	public void close(){
		if(null != producer){
			producer.close();
		}
	}


	public void init(Properties props){
		
	    String zkServers = null;
	    String kafkaBrokers=null;
		 if(!props.containsKey("acks")){
			 props.put("acks", "1");
		 }
		 if(!props.containsKey("retries")){
			 props.put("retries", 1);
		 }
		 if(!props.containsKey("batch.size")){
			 props.put("batch.size", 16384);
		 }
		 if(!props.containsKey("linger.ms")){
			 props.put("linger.ms", 1000);
		 }
		 if(!props.containsKey("buffer.memory")){
			 props.put("buffer.memory", 33554432);
		 }
		 if(!props.containsKey("key.serializer")
				 || StringUtils.isBlank(props.getProperty("key.serializer"))){
			 props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		 }
		 if(!props.containsKey("value.serializer")
				 || StringUtils.isBlank(props.getProperty("value.serializer"))){
			 props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		 }
		  if (props.containsKey("zookeeper.connect")) {
		       zkServers = props.getProperty("zookeeper.connect");
		       kafkaBrokers = kafaZKUtil.getBrokers(zkServers);
		      props.put("bootstrap.servers", kafkaBrokers);
		      log.info("find kafka brokers: " + kafkaBrokers + ",zkServers: " + zkServers);
		    }
		 producer = new org.apache.kafka.clients.producer.KafkaProducer<Object, Object>(props);
		 
	}

	public boolean isNormal(){
		return this.status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

}
