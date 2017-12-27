package com.xmj.tool.util.xkafka;


import com.xmj.tool.util.xkafka.service.DataLoadFromKafkaService;
import com.xmj.tool.util.xkafka.service.DataSendToKafkaService;

public class TestAction {

	public void send(){
		DataSendToKafkaService send = new DataSendToKafkaService("zookeeper_connect", "topic");
		for(int i=0;i<10;i++){
			Object content = i;
			send.addQueue(content);
		}
				
	}
	
	public void load(){
		DataLoadFromKafkaService load = new DataLoadFromKafkaService("zookeeper_connect", "topic");
		while(true){
			load.getQueue();
		}
	}
	
	
}
