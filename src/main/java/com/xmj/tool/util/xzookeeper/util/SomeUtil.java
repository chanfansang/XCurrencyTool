package com.xmj.tool.util.xzookeeper.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SomeUtil {
	private static final Log log = LogFactory.getFactory().getInstance(SomeUtil.class);
	
	public static Object byteToJson(byte[] byteInfo) {
		JSONObject obj = null;
		try {
			String jsonStr = new String(byteInfo, "utf-8");
			obj=JSON.parseObject(jsonStr);
		} catch (Exception e) {
			log.error(e);
		}
		return obj;
	}
	
	public static byte[] getByte(Object target) {
		byte[] tmpByte = null;
		if(target instanceof String){
			try {
				tmpByte = ((String) target).getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e);
			}
		}else{
			String jsonStr = JSON.toJSONString(target);
			
			try {
				tmpByte = jsonStr.getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e);
			}
		}
		
		return tmpByte;
	}
}
