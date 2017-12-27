package com.xmj.tool.service.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.xmj.demo.common.data.json.Xjson;
import com.xmj.demo.common.data.string.XMap;
import com.xmj.demo.common.data.string.XString;
import com.xmj.demo.util.api.ResourceSearchUtil;

@RestController
public class ResourceApi {

	private static Log log = LogFactory.getFactory().getInstance(ResourceApi.class);
	private static String result = null;
	private XMap xmap = new XMap<>();
	private Xjson xjson = new Xjson();
	private ResourceSearchUtil searchUtil = new ResourceSearchUtil();
	
	@RequestMapping(value = "/resource/search", method = RequestMethod.POST)
	//@ResponseBody
	public JSONArray searchResourceApi(HttpServletRequest request){
		
		String str = XString.getParamter(request);
		System.out.println(str);
		Map<String,Object> map = xjson.getJson(str).oneLayerJson("searchStr","s","t","page");
		String searchStr = map.get("searchStr").toString();
		String s = xmap.getMap(map).getString("s");
		String t = xmap.getMap(map).getString("t");
		String page = xmap.getMap(map).getString("page");
		System.out.println(searchStr+s+t+page);
		
		JSONArray jsonStr = searchUtil.search(searchStr, s, t, page);
		//System.out.println(jsonStr);
		//jsonStr = jsonStr.replace("\"link\"", "link").replace("\"name\"", "name").replace("\"pwd\"", "pwd").replace("&amp;", "&");
		return jsonStr;
	}
	
	
	
}
