package com.xmj.demo.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmj.demo.common.parameter.Global;
import com.xmj.demo.util.token.TokenUtil;
import com.xmj.demo.vo.Message;

@Controller
public class SendService {

	private static Log log = LogFactory.getFactory().getInstance(SendService.class);
	
	private static final long MAX_TOKEN_EFFECTIVE_TIME = 90 * 60 * 1000;

	private String token = null;

	private long lastGetTokenTime;

	private TokenUtil tokenUtil;
	
	@RequestMapping("demo")
	public String demo(Model model){
		return "demo";
	}
	
	
	@RequestMapping("synCheck")
	@ResponseBody
	public Object synCheck(@RequestBody Message msg){
		//String tokenTmp = getToken(Global.LOGIN_APPID, Global.LOGIN_SECRET);
		String url = Global.API_GATE_WAY_ADDRESS + Global.ASYN_SERVICE_NAME + "/syn";
		//String url = "http://10.31.2.44:8765/boco_asyn_and_ftp_service/syn";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		//post.addHeader("token", tokenTmp);
		post.addHeader("AppID", Global.LOGIN_APPID);
		post.addHeader("SessionID", "asseed" + System.currentTimeMillis());
		HttpResponse response = null;
		try {
			String jsonString = JSON.toJSONString(msg);  
			StringEntity s = new StringEntity(jsonString);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
			System.out.println("login url[" + url + "]");
//			System.out.println("token[" + tokenTmp + "]");
			System.out.println("AppID[" + Global.LOGIN_APPID + "]");
		
			response = client.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("request error", e);
		}

		String res = null;
		try {
			res = EntityUtils.toString(response.getEntity(), "utf-8");
			System.out.println("login result:" + res);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("get response error", e);
		}
		JSONObject object = JSON.parseObject(res);
		

		return object;
	}
	
	
	@RequestMapping("asynCheck")
	@ResponseBody
	public Object asynCheck(@RequestBody Message msg){
		//String tokenTmp = getToken(Global.LOGIN_APPID, Global.LOGIN_SECRET);
		String url = Global.API_GATE_WAY_ADDRESS + Global.ASYN_SERVICE_NAME + "/asyn/ftp";
		//String url = "http://10.31.2.44:8765/boco_asyn_and_ftp_service/asyn/ftp";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		//post.addHeader("token", tokenTmp);
		post.addHeader("AppID", Global.LOGIN_APPID);
		post.addHeader("SessionID", "asseed" + System.currentTimeMillis());
		HttpResponse response = null;
		try {
			String jsonString = JSON.toJSONString(msg);  
			StringEntity s = new StringEntity(jsonString);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
			System.out.println("login url[" + url + "]");
//			System.out.println("token[" + tokenTmp + "]");
			System.out.println("AppID[" + Global.LOGIN_APPID + "]");
		
			response = client.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("request error", e);
		}

		String res = null;
		try {
			res = EntityUtils.toString(response.getEntity(), "utf-8");
			System.out.println("login result:" + res);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("get response error", e);
		}
		JSONObject object = JSON.parseObject(res);
		

		return object;
	}
	
	
	private String getToken(String appId, String appSecret) {
		boolean getTokenFlag = false;
		if (token == null) {
			getTokenFlag = true;
		} else {
			long now = System.currentTimeMillis();
			if (now - lastGetTokenTime > MAX_TOKEN_EFFECTIVE_TIME) {
				getTokenFlag = true;
			}
		}
		if (getTokenFlag) {
			token = tokenUtil.getToken(appId, appSecret);
		}
		return new String(token);
	}
}
