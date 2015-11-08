package com.aechackathon.mobifm.proximity;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.aechackathon.mobifm.Main;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProximityAdapter {
	
	public ProximityAdapter() {
	}	
	
	public Map<String, Object> getEvents() throws JsonParseException, JsonMappingException, IOException {		
		return getJsonResponse("events.json");
	}
	
	public Map<String, Object> getDepartments() throws JsonParseException, JsonMappingException, IOException {		
		return getJsonResponse("departments.json");
	}
	
	public Map<String, Object> getDevices() throws JsonParseException, JsonMappingException, IOException {		
		return getJsonResponse("inputs.json");
	}
	
	public Map<String, Object> getDeviceByName(String name) throws JsonParseException, JsonMappingException, IOException {
		return getDevices()
			.entrySet()
			.stream()
			.filter(x -> ((Map<?,?>)x.getValue()).get("name").equals(name))
			.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	}
	
	public static String getDepartmentIdOfDevice(Map<?, ?> device) {
		return (String)device.get("departmentUID");
	}
	
	public Map<String, Object> getDepartmentById(String id) throws JsonParseException, JsonMappingException, IOException {
		return getDepartments()
				.entrySet()
				.stream()
				.filter(x -> x.getKey().equals(id))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	}

	public Map<String, Object> getAnonymousVisitors() throws JsonParseException, JsonMappingException, IOException {
		return getJsonResponse("anonymousVisitors.json");		
	}
	
	private Map<String, Object> getJsonResponse(String path) throws JsonParseException, JsonMappingException, IOException {
		URI uri = UriBuilder
				.fromUri("https://{host}/organizations/{organization}/{path}?auth={auth}")
				.resolveTemplate("host", Main.getConfigProperty("proximity.host"))				
				.resolveTemplate("organization", Main.getConfigProperty("proximity.organization"))				
				.resolveTemplate("auth", Main.getConfigProperty("proximity.auth"))				
				.resolveTemplate("path", path)
				.build();
		
//		System.out.println(uri);

		Client client = ClientBuilder.newClient();		
		WebTarget target = client.target(uri);		
		Builder builder = target.request().accept(MediaType.APPLICATION_JSON);		
		Response response = builder.get();
		
		if (response.getStatus() != 200) {
			throw new RuntimeException("HTTP response error code: " + response.getStatusInfo());
		}
		
		String jsonString = response.readEntity(String.class);
//		Map<String, Object> jsonObject = response.readEntity(new GenericType<Map<String,Object>>(){});
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
		return jsonObject;

	}

	private void getThingseeData(String path) throws JsonParseException, JsonMappingException, IOException {


		Client client = ClientBuilder.newClient();	
		WebTarget target2 = ( client
				.target(Main.getConfigProperty("thingsee.host"))
				.path(Main.getConfigProperty("thingsee.path")));
				
		
		Builder builder2 = target2.request();
		builder2.accept(MediaType.APPLICATION_JSON);
		builder2.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0cyI6MTQ0NjkxNDIwODM2NywidXVpZCI6ImE4NzliZGQwLTg1NjUtMTFlNS05YjRiLTg5NzczZmEwMzIxNCIsInNjb3BlIjpbImFsbDphbGwiXSwiaWF0IjoxNDQ2OTE0MjA4LCJleHAiOjE0NDc1MTkwMDh9.fewleb-PJZULfigRRDfJ_LFgNM8PKcOhdHaO-aSJLck");
		String jsonString2 = builder2.get(String.class);
		

		Object obj=JSONValue.parse(jsonString2);
		JSONObject jsonObject2 = (JSONObject) obj;

		JSONArray events = (JSONArray) jsonObject2.get("events");
		System.out.println(events);

		JSONObject lastEvent = (JSONObject) events.get(0);
		JSONObject cause = (JSONObject) lastEvent.get("cause");
		
		JSONArray senses = (JSONArray) cause.get("senses");
		
		
		Double temperature = 0.0;
		Double humidity = 0.0;
		Double pressure = 0.0;
		
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
                humidity = val;
                break;
            case "0x00060200":
                temperature = val;
                break;
            case "0x00060400":
                pressure = val;
                break;
                
            }
		}

		System.out.println("Temperature: " + temperature + " Humidity: " + humidity + " Pressure: " + pressure);


	}
	
}
