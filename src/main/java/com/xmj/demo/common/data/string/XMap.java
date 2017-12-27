package com.xmj.demo.common.data.string;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class XMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

	transient Set<Map.Entry<K, V>> entrySet;
	private Map<K,V> map = new HashMap<>();
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set es = this.entrySet;
		return es;
	}
	
	public XMap<K, V> getMap(Map<K,V> map){
		this.map = map;
		//System.out.println("into getMap");
		return this;		
	}
	
	public String getString(Object o){
		String result = null;
		Object obj = map.get(o);
		if(obj instanceof Object[]){
			List<Object> list = java.util.Arrays.asList(obj);
			result = list.toString();
		}else{
			result = obj.toString();
		}
		
		return result;
		
	}
	
	
	
}
