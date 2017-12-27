package com.xmj.tool.util.xkafka.util;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class kafaZKUtil {
	
	  public static String getBrokers(String zkServers)
	  {
	    StringBuffer buffer = new StringBuffer();
	    ZkClient zkClient = null;
	    try {
	      zkClient = new ZkClient(zkServers, 5000);
	      zkClient.setZkSerializer(new ZkStringSerializer());
	      String offsetPath = "/brokers/ids";
	      
	      List<String> brokerIds = zkClient.getChildren(offsetPath);
	      for (int i = 0; i < brokerIds.size(); i++) {
	        String brokerId = (String)brokerIds.get(i);
	        String brokerInfo = (String)zkClient.readData(offsetPath + "/" + brokerId);
	        JSONObject jsonObject = JSON.parseObject(brokerInfo);
	        String host = jsonObject.getString("host");
	        String port = jsonObject.getString("port");
	        if (i == brokerIds.size() - 1)
	          buffer.append(host).append(":").append(port);
	        else
	          buffer.append(host).append(":").append(port).append(",");
	      }
	    }
	   finally {
	      if (zkClient != null) {
	        zkClient.close();
	      }
	    }
	    return buffer.toString();
	  }
}
