package com.xmj.tool.util.xhdfs.test;

import java.net.FileNameMap;
import java.net.URLConnection;

public class Test {

	public static void main(String[] args) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();  
	    String type = fileNameMap.getContentTypeFor("C:/Users/x1280/Desktop/test.xml");  
	    System.out.println(type);
	}
}
