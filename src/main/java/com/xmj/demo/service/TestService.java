package com.xmj.demo.service;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestService {

	private static Log log = LogFactory.getFactory().getInstance(TestService.class);
	private static String result = null;
	private String data = null;
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@RequestMapping(value = "/asynftpResult", method = RequestMethod.POST)
	public void testService(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		result=null;
		System.out.println("*****1*************");
		String str, wholeStr = "";
		BufferedReader br;
		try {
			System.out.println("*****2*************");
			br = request.getReader();	
			System.out.println("******3************");
			while((str = br.readLine()) != null){
				wholeStr += str;
			}
			log.info("body:"+wholeStr);
			System.out.println("body:"+wholeStr);
			setData(wholeStr);
			//result = wholeStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//return wholeStr;
	}
	
	@RequestMapping(value = "/result", method = RequestMethod.POST)
	public String clientInvoke(HttpServletRequest request, HttpServletResponse response){
		String result = null;
		while(true){
			if(getData()!=null){
				result = getData();
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		data = null;
		return result;
	}
	
	
	
}
