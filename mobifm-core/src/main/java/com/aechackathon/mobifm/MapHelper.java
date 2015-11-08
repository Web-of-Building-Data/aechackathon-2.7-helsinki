package com.aechackathon.mobifm;

import java.util.Map;
import java.util.Map.Entry;

public class MapHelper {
	
	public static <K,V> Entry<K,V> getFirstEntry(Map<K,V> map) {
		return map.entrySet().iterator().next();
	}	

	public static <K, V> K getFirstKey(Map<K,V> map) {
		return map.keySet().iterator().next();
	}

	public static <K, V> Object getFirstValue(Map<K,V> map) {
		return map.values().iterator().next();
	}
	
}