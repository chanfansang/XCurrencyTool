package com.xmj.tool.util.xzookeeper.test.test3.util;

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
		String jsonStr = JSON.toJSONString(target);
		byte[] tmpByte = null;
		try {
			tmpByte = jsonStr.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
		return tmpByte;
	}
}
