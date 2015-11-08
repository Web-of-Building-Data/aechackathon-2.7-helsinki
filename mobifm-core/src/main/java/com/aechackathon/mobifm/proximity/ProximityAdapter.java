package com.aechackathon.mobifm.proximity;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

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
	
}
