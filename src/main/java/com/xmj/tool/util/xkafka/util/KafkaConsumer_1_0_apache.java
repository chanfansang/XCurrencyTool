package com.xmj.tool.util.xkafka.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;


public class KafkaConsumer_1_0_apache {


	private org.apache.kafka.clients.consumer.KafkaConsumer<Object, Object> consumer = null;
	private Logger log = null;
	
	public KafkaConsumer_1_0_apache(Logger log){
		this.log = log;
	}
	
	public void init(Properties props) throws Exception{
		
		 if(!props.containsKey("group.id")
				 || StringUtils.isBlank(props.getProperty("group.id"))){
			 props.put("group.id", "gateWay");
		 }
		 
		 if(!props.containsKey("enable.auto.commit")
				 || StringUtils.isBlank(props.getProperty("enable.auto.commit"))){
			  props.put("enable.auto.commit", true);
		 }
		 
		 if(!props.containsKey("auto.commit.interval.ms")//int
				 || StringUtils.isBlank(props.getProperty("auto.commit.interval.ms"))){
			  props.put("auto.commit.interval.ms", 5000);
		 }else{
			 props.put("auto.commit.interval.ms", Integer.valueOf(props.getProperty("auto.commit.interval.ms")));
		 }
		 
		 if(!props.containsKey("key.deserializer")
				 || StringUtils.isBlank(props.getProperty("key.deserializer"))){
			 props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		 }
		 
		 if(!props.containsKey("value.deserializer")
				 || StringUtils.isBlank(props.getProperty("value.deserializer"))){
			 props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		 }
		 
		 if(!props.containsKey("heartbeat.interval.ms")//int
				 || StringUtils.isBlank(props.getProperty("heartbeat.interval.ms"))){
			 props.put("heartbeat.interval.ms", 3000);
		 }else{
			 props.put("heartbeat.interval.ms", Integer.valueOf(props.getProperty("heartbeat.interval.ms")));
		 }
		 
		 if(!props.containsKey("session.timeout.ms")//int
				 || StringUtils.isBlank(props.getProperty("session.timeout.ms"))){
			 props.put("session.timeout.ms", 30000);
		 }else{
			 props.put("session.timeout.ms", Integer.valueOf(props.getProperty("session.timeout.ms")));
		 }
		 
		 if(!props.containsKey("max.poll.records")//int
				 || StringUtils.isBlank(props.getProperty("max.poll.records"))){
			 props.put("max.poll.records", 10000);
		 }else{
			 props.put("max.poll.records", Integer.valueOf(props.getProperty("max.poll.records")));
		 }
		 
		 if(!props.containsKey("fetch.max.bytes")//int
				 || StringUtils.isBlank(props.getProperty("fetch.max.bytes"))){
			 props.put("fetch.max.bytes", 52428800);
		 }else{
			 props.put("fetch.max.bytes", Integer.valueOf(props.getProperty("fetch.max.bytes")));
		 }
		 
		 if(!props.containsKey("receive.buffer.bytes")//int   -1就是用OS的最大网络缓存
				 || StringUtils.isBlank(props.getProperty("receive.buffer.bytes"))){
			 props.put("receive.buffer.bytes", 65536);
		 }else{
			 props.put("receive.buffer.bytes", Integer.valueOf(props.getProperty("receive.buffer.bytes")));
		 }
		
		 if (props.containsKey("zookeeper.connect")) {
		      String zkServers = props.getProperty("zookeeper.connect");
		      String kafkaBrokers = kafaZKUtil.getBrokers(zkServers);
		      props.put("bootstrap.servers", kafkaBrokers);
		      log.info("find kafka brokers: " + kafkaBrokers + ",zkServers: " + zkServers);
		    }
		 
		 if(!props.containsKey("bootstrap.servers")
				 || StringUtils.isBlank(props.getProperty("bootstrap.servers")) ){
			 throw new Exception("no bootstrap.servers");
		 }
		 this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<Object, Object>(props);
	     
	}
	
	public void close(){
		if(consumer != null){
			this.consumer.close();
		}
	}
	
	public void seek(String topic, int partitionNum, long Offset) {
		List<TopicPartition> topicPartitionList = new ArrayList<TopicPartition>();
		for(int i = 0; i < partitionNum; i++){
			TopicPartition topicPartition = new TopicPartition(topic,i);
			topicPartitionList.add(topicPartition);
		}
		consumer.assign(topicPartitionList);
		for(int i = 0; i < topicPartitionList.size(); i++){
		 consumer.seek(topicPartitionList.get(i), Offset);
		}
	    
	}
	
	public void assign(String topic, List<Integer> partitions) {
		
		List<TopicPartition> topicPartitionList = new ArrayList<TopicPartition>();
		for(int i = 0; i < partitions.size(); i++){
			TopicPartition topicPartition = new TopicPartition(topic,partitions.get(i));
			topicPartitionList.add(topicPartition);
		}
		 consumer.assign(topicPartitionList);
	}
	
	public void assign(String topic, int partition, long Offset) {
		 TopicPartition topicPartition = new TopicPartition(topic,partition);
	     consumer.seek(topicPartition, Offset);
	}
	public void receive(List<ConsumerRecord<Object, Object>> list){
		  ConsumerRecords<Object, Object> records = null;
			try {
				records = consumer.poll(1000);
			} catch (Exception e) {
				log.error("",e);
			}
			if(null == records){
				return;
			}
	        try {
				 for (ConsumerRecord<Object, Object> record : records)
//				     System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), (String)record.key(), (String)record.value());
					 list.add(record);
			} catch (Exception e) {
				log.error("",e);
			}
	}

	public void subscribe(String topics,String especial_partitions) {
		if(StringUtils.isBlank(especial_partitions)){
			 consumer.subscribe(Arrays.asList(topics));
			 log.info("subscribe topics[" + topics + "] all partitions");
		}else{
			String[] especial_partition = especial_partitions.split(",");
			List<Integer> partitions = new ArrayList<Integer>();
			for(int i = 0; i < especial_partition.length; i++){
				try {
					partitions.add(Integer.valueOf(especial_partition[i]));
				} catch (NumberFormatException e) {
					log.error("the [" + i + "] partition value [" + especial_partition[i] + "]is not Integer");
				}
			}
			String[] topic = topics.split(",");
			for(int i = 0; i < topic.length; i++){
				this.assign(topic[i], partitions);
				log.info("subscribe topic[" + topic[i] + "]  partitions[" + especial_partitions + "]");
			}
			
		}
		
	}

}
