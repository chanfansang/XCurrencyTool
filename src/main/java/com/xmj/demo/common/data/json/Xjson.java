package com.xmj.demo.common.data.json;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Xjson {

	private JSONObject object;
	
	public  Xjson getJson(String jsonStr){
		//System.out.println("into getJson");
		JSONObject obj = JSON.parseObject(jsonStr);
		this.object = obj;		
		return this;
	}
	
	//public 
	
	public String toJsonString(Object obj){
		String jsonstr = JSON.toJSONString(obj);
		
		return jsonstr;
	}
	public JSONArray toJsonArray(Object obj){
		String jsonstr = JSON.toJSONString(obj);
		JSONArray j = JSON.parseArray(jsonstr);
		return j;
	}
	
	public Map<String,Object> oneLayerJson(String ...strs){
		Map<String,Object> map = new HashMap<String,Object>();
		Object obj = null;
		for(String str : strs){
			//System.out.println(str);
			obj = object.get(str);
			map.put(str, obj);
		}
		return map;
	}
	
}
