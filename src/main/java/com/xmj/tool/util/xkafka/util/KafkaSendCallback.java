package com.xmj.tool.util.xkafka.util;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class KafkaSendCallback implements Callback {
	private static Logger logger = LoggerFactory.getLogger(KafkaSendCallback.class);
	
	private ProducerRecord<Object, Object> record = null;
	private KafkaProducer_1_0_apache producer_apache = null;
	public KafkaSendCallback(ProducerRecord<Object, Object> record_,KafkaProducer_1_0_apache producer_apache_){
		this.record = record_;
		this.producer_apache = producer_apache_;
	}

     @Override
     public void onCompletion(RecordMetadata recordMetadata, Exception e) {
    	 
    	 
         //send success
         if (null == e) {
//             String meta = "topic:" + recordMetadata.topic() + ", partition:"
//                     + recordMetadata.topic() + ", offset:" + recordMetadata.offset();
             logger.info("******send message success, record[" + record.toString() + "]");
             return;
         }
         //send failed
         logger.error("send message failed,record[" + record.toString() + "]",e);
        
         producer_apache.setStatus(false);
     }
 }
