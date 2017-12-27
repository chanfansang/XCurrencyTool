package com.xmj.demo.util.api;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xmj.demo.common.data.json.Xjson;
import com.xmj.demo.vo.ResourceSearchVO;


public class ResourceSearchUtil {

	protected static Matcher matcher = null;
	private Xjson xjson = new Xjson();
	private static boolean match(String str, String regex) {
		Pattern p = Pattern.compile(regex);
		matcher = p.matcher(str);
		return matcher.find();
	}
	
	public JSONArray search(String searchStr, String s, String t, String page) {
		JSONArray result = null;
		List<ResourceSearchVO> listvo = new ArrayList<ResourceSearchVO>();
		String value;
		boolean is = false;
		try {
			value = URLEncoder.encode(searchStr, "utf-8");
			// sc.nextLine();
			//boolean is = false;
			//List<String> list = new ArrayList<String>();
			String reg1 = "<strong>\\s+.*?</strong>";
			String reg2 = "\\S+提取密码.*";
			String reg3 = "<div style=\"clear: both\"></div>";
			// String reg = "<strong>\\s+.*?</strong>";
			Pattern pattern = Pattern.compile(reg1);
			//System.out.println("page=" + page);
			//while(!is){
				
			String url = "http://www.fastsoso.cn/search/" + page + "?k=" + value + "&t=-1&s=2";
			System.out.println(url);
			String aa = getUrl(url);

			String line = null;
			byte[] by = aa.getBytes();
			InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(by));
			BufferedReader reader = new BufferedReader(isr);
			is = true;
			while (null != (line = reader.readLine())) {
				ResourceSearchVO vo = new ResourceSearchVO();
				if (match(line, reg1)) {
					is = false;
					//System.out.println("reg1->" + line);
					vo.setLink(line);
					vo.setPwd("");
					while (null != (line = reader.readLine())) {
						// System.out.println("line->"+line);
						if (match(line, reg2)) {
							//System.out.println("2222222222222222222222222222");
							while (null != (line = reader.readLine())) {
								// System.out.println("333333->"+line);
								vo.setPwd(line);

								break;
							}
						}
						if (match(line, reg3)) {
							break;
						}

					}
					listvo.add(vo);
					//System.out.println("**************一个结束");
				}

			}
				
			//}
			//System.out.println("当page="+(page-1)+"时，就没有内容了");
			

			System.out.println(listvo.size());
			List<ResourceSearchVO> list = new ArrayList<ResourceSearchVO>();
			for (ResourceSearchVO v : listvo) {
				//System.out.println(v.getLink());
				//if (v.getPwd() != null) {
				//	System.out.println(v.getPwd());
				//}
				list.add(get(v));

				//System.out.println("*********************************");

			}
			result = xjson.toJsonArray(list);
			// System.out.println("当page="+(page-1)+"时，就没有内容了");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public JSONArray search_no_page(String searchStr, String s, String t) {
		JSONArray result = null;
		List<ResourceSearchVO> listvo = new ArrayList<ResourceSearchVO>();
		int page = 1;
		String value;
		boolean is = false;
		try {
			value = URLEncoder.encode(searchStr, "utf-8");
			// sc.nextLine();
			//boolean is = false;
			//List<String> list = new ArrayList<String>();
			String reg1 = "<strong>\\s+.*?</strong>";
			String reg2 = "\\S+提取密码.*";
			String reg3 = "<div style=\"clear: both\"></div>";
			// String reg = "<strong>\\s+.*?</strong>";
			Pattern pattern = Pattern.compile(reg1);
			//System.out.println("page=" + page);
			while(!is){
				
				String url = "http://www.fastsoso.cn/search/" + page + "?k=" + value + "&t=-1&s=2";
				System.out.println(url);
				String aa = getUrl(url);

				String line = null;
				byte[] by = aa.getBytes();
				InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(by));
				BufferedReader reader = new BufferedReader(isr);
				is = true;
				while (null != (line = reader.readLine())) {
					ResourceSearchVO vo = new ResourceSearchVO();
					if (match(line, reg1)) {
						is = false;
						//System.out.println("reg1->" + line);
						vo.setLink(line);
						vo.setPwd("");
						while (null != (line = reader.readLine())) {
							// System.out.println("line->"+line);
							if (match(line, reg2)) {
								//System.out.println("2222222222222222222222222222");
								while (null != (line = reader.readLine())) {
									// System.out.println("333333->"+line);
									vo.setPwd(line);

									break;
								}
							}
							if (match(line, reg3)) {
								break;
							}

						}
						listvo.add(vo);
						//System.out.println("**************一个结束");
					}

				}
				page++;
				if(page == 3){
					break;
				}
				
				
			}
			System.out.println("当page="+(page-1)+"时，就没有内容了");
			

			System.out.println(listvo.size());
			List<ResourceSearchVO> list = new ArrayList<ResourceSearchVO>();
			for (ResourceSearchVO v : listvo) {
				//System.out.println(v.getLink());
				//if (v.getPwd() != null) {
				//	System.out.println(v.getPwd());
				//}
				list.add(get(v));

				//System.out.println("*********************************");

			}
			result = xjson.toJsonArray(list);
			// System.out.println("当page="+(page-1)+"时，就没有内容了");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	public ResourceSearchVO get(ResourceSearchVO v){
		ResourceSearchVO vo = new ResourceSearchVO();
		String regUrlandName = "\\s+href=\"(.*)?\".*?target=\"_blank\">(.*)?</a>";
		v.getLink();
		String b = "<span style=\"color: #0000FF;\">we9f</span>";
		String a = "        <strong> <a rel=\"noreferrer\" href=\"https://pan.baidu.com/share/link?uk=3937394074&amp;shareid=968061300\" target=\"_blank\"> [中国]《<span style=\"color:red;\">战</span><span style=\"color:red;\">狼</span><span style=\"color:red;\">2</span>》2017 </a> <span class=\"badge\">加密分享</span> </strong> ";
		String s1 = null;
		String s2 = null;
		String s3 = "无密码";
		if (match(v.getLink(), regUrlandName)) {
			s1 = matcher.group(1);
			s2 = matcher.group(2);
			//System.out.println(matcher.group(2));
		}
		s2 = s2.replace("<span style=\"color:red;\">", "").replace("</span>", "");
		System.out.println(s1);
		System.out.println(s2);
		if(!v.getPwd().isEmpty()){
			//if (match(v.getMima(), regPwd)) {}
			s3 = b.split(">")[1].split("<")[0];
		}
		
		System.out.println(s3);
		vo.setLink(s1);
		vo.setName(s2);
		vo.setPwd(s3);
		//JSONArray json = JSONArray.fromObject(vo);
		
		return vo;
	}
	
	public static String getUrl(String url) {
		List<String> fs_id = new ArrayList<String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection c = Jsoup.connect(url);
		Document doc = null;
		try {
			Thread.sleep(2000);
			doc = c.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = doc.toString();
		// System.out.println("html:"+html);
		return html;
	}
}
