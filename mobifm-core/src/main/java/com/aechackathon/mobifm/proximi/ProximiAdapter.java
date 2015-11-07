package com.aechackathon.mobifm.proximi;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.json.JsonArray;
import javax.print.attribute.standard.Media;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.aechackathon.mobifm.Main;

public class ProximiAdapter {
	
	public Object getData() {
		
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client
				.target(Main.getConfigProperty("proximi.host"))
				.path(Main.getConfigProperty("proximi.path"))
				.queryParam("auth", Main.getConfigProperty("proximi.queryparam.auth"));
		
		
//		ClientConfig cfg = new DefaultClientConfig(GensonJsonConverter.class);
//		Client client = Client.create(cfg);
//		WebResource webResource = client.resource("http://path/to/service");

		// you can map it to a pojo, no need to have a string or map
//		SomePojo pojo = webResource
//		                .accept(MediaType.APPLICATION_JSON)
//		                .get(SomePojo.class);
		
		Builder builder = target.request();
		builder.accept(MediaType.APPLICATION_JSON);
		
		String jsonString = builder.get(String.class);
		
		JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
		
		for (HashMap.Node Node entry : jsonObject.entrySet()) {
			System.out.println(entry.getClass() + " " + entry);
		}
		
//		System.out.println(jsonObject.);
		
		return null;
		
		
	}
	
	
	private static String getBaseUri() {
		return Main.getConfigProperty("proximi.baseuri");
	}
	
	private static String getPath() {
		return Main.getConfigProperty("proximi.path");
	}

}
