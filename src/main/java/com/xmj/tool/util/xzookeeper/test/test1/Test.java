package com.xmj.tool.util.xzookeeper.test.test1;

public class Test {
	public static void main(String[] args) {
		String a = "/a/b/c.zip";
		a = a.substring(0,a.lastIndexOf("/"));
		System.out.println(a);
	}
}
