package com.aechackathon.mobifm.data;

import java.util.Map;

import com.aechackathon.mobifm.MapHelper;

public class Department {
	
	private final String id;
	private final Map<?, ?> properties;
	
	public Department(Map<?, ?> properties) {
		this.id = (String)MapHelper.getFirstKey(properties);
		this.properties = (Map<?, ?>)MapHelper.getFirstValue(properties);
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return (String)properties.get("name");
	}

	public Map<?,?> getProperties() {
		return properties;
	}
	
	
	@Override
	public String toString() {
		return String.format("{type=%s, id=%s, properties={%s}}", getClass(), getId(), getProperties());
	}
	
}
