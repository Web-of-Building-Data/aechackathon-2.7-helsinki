package com.aechackathon.mobifm.data;

import java.util.Map;

import com.aechackathon.mobifm.MapHelper;

public class Device {
	
	private final String id;
	private final Map<?, ?> properties;
	
	public Device(Map<?, ?> properties) {
		this.id = (String)MapHelper.getFirstKey(properties);
		this.properties = (Map<?, ?>)MapHelper.getFirstValue(properties);
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return (String)properties.get("name");
	}
	
	public String getDepartmentId() {
		return (String)properties.get("departmentUID");
	}
	
	public Map<?,?> getProperties() {
		return properties;
	}
	
	public double getLatitude() {
		Map<?,?> map = (Map<?,?>)((Map<?,?>)properties.get("data")).get("map");
		return (Double)map.get("lat");
	}
	
	public double getLongtitude() {
		Map<?,?> map = (Map<?,?>)((Map<?,?>)properties.get("data")).get("map");
		return (Double)map.get("lng");
	}
	
	
	
	@Override
	public String toString() {
		return String.format("{type=%s, id=%s, properties={%s}}", getClass(), getId(), getProperties());
	}

}
