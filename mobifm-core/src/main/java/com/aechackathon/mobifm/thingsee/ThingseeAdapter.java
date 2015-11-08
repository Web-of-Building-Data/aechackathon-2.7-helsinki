package com.aechackathon.mobifm.thingsee;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.aechackathon.mobifm.Main;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ThingseeAdapter {

	public ThingseeAdapter() {
	}	
	
	public Map <String, Double> getThingseeData() throws JsonParseException, JsonMappingException, IOException {

		Client client = ClientBuilder.newClient();	
		WebTarget target = ( client
				.target(Main.getConfigProperty("thingsee.host"))
				.path(Main.getConfigProperty("thingsee.path")));
				
		
		Builder builder = target.request();
		builder.accept(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0cyI6MTQ0NjkxNDIwODM2NywidXVpZCI6ImE4NzliZGQwLTg1NjUtMTFlNS05YjRiLTg5NzczZmEwMzIxNCIsInNjb3BlIjpbImFsbDphbGwiXSwiaWF0IjoxNDQ2OTE0MjA4LCJleHAiOjE0NDc1MTkwMDh9.fewleb-PJZULfigRRDfJ_LFgNM8PKcOhdHaO-aSJLck");
		String jsonString = builder.get(String.class);
		
		Object obj=JSONValue.parse(jsonString);
		JSONObject jsonObject = (JSONObject) obj;

		JSONArray events = (JSONArray) jsonObject.get("events");

		JSONObject lastEvent = (JSONObject) events.get(0);
		JSONObject cause = (JSONObject) lastEvent.get("cause");
		
		JSONArray senses = (JSONArray) cause.get("senses");
		
		
		Double temperature = 0.0;
		Double humidity = 0.0;
		Double pressure = 0.0;
		Double luminance = 0.0;
		
		Iterator<JSONObject> iterator = senses.iterator();
		while (iterator.hasNext()) {
			JSONObject reading = iterator.next();
			String id = (String) reading.get("sId");
			Double val = 0.0;
			try {
				val = (Double) reading.get("val");
			}			
			catch (Exception e)
			{
			}
		
			switch (id){
            case "0x00060100":
                temperature = val;
                break;
            case "0x00060200":
                humidity = val;
                break;  
            case "0x00060300":
                luminance = val;
                break;
            case "0x00060400":
                pressure = val;
                break;
                
            }
		}

		Map <String, Double> hm = new HashMap <String, Double>();
		hm.put("temperature", temperature);
		hm.put("humidity", humidity);
		hm.put("pressure", pressure);
		hm.put("luminance", luminance);
		
		return hm;
	}
}
