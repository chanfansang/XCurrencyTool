package com.xmj.tool.service.util.token;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GetTokenByAPIGW implements TokenUtil {

	String postURL = "/accessToken";

	public GetTokenByAPIGW(String APIGateWayAddress) {
		postURL = APIGateWayAddress + postURL;
	}

	public String getToken(String appID, String secret) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(postURL);
		String authSrc = appID + ":" + secret;
		String auth = base64(authSrc);
		auth = "Basic " + auth;
		get.addHeader("Authorization", auth);

		HttpResponse response = null;
		try {
			response = client.execute(get);
		} catch (Exception e) {
			System.out.println("request token error,appid[" + appID + "],secret[" + secret + "]");
			e.printStackTrace();
		}

		String res = null;
		try {
			res = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	private String base64(String authSrc) {
		return new String(Base64.encodeBase64(authSrc.getBytes()));
	}
}
