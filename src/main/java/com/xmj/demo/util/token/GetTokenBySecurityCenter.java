package com.xmj.demo.util.token;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class GetTokenBySecurityCenter implements TokenUtil {

	private String baseURL = "/bss/oauth2.0/accessToken?grant_type=password&client_id=bss";

	public GetTokenBySecurityCenter(String securityCenterAddress) {
		baseURL = securityCenterAddress + baseURL;
	}

	public String getToken(String appID, String password) {
		String url = baseURL + "&username=" + appID + "&password=" + password;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (Exception e) {
			System.out.println("request token error,appid[" + appID + "],secret[" + password + "]");
			e.printStackTrace();
		}

		String res = null;
		try {
			res = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = (JSONObject) JSON.parse(res);
		if (null == jsonObject) {
			System.out.println("get token error");
			return null;
		}
		String token = jsonObject.getString("access_token");
		return token;
	}

	public static void main(String[] args) {
		GetTokenBySecurityCenter center = new GetTokenBySecurityCenter("http://120.131.0.92:18080");

		System.out.println(center.getToken("sherry", "sherry"));
//		System.out.println(center.getToken("liyong1", "1497364751206"));
	}

}
