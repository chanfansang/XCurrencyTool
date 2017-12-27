package com.xmj.tool.service.common.data.string;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class XString {

	
	public static String getParamter(HttpServletRequest request){
		String result = null;
		String str, wholeStr = "";
		BufferedReader br;
		//System.out.println("111");
		try {
			br = request.getReader();	
			while((str = br.readLine()) != null){
				wholeStr += str;
			}
			//System.out.println("body:"+wholeStr);
			
			result = wholeStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
