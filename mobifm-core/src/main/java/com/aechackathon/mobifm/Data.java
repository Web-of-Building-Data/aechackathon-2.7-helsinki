package com.aechackathon.mobifm;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Root resource (exposed at "data" path)
 */
@Path("data")
public class Data {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @SuppressWarnings("serial")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getData() {
    	Map<String, Object> data = new HashMap<String, Object>() {{
    		put("location", new HashMap<String, Object>() {{
        		put("name", "Auditorium");
        		put("guid", "2iJMN5fwTCXRXnQXOq5A8l");
        		put("absolute", new HashMap<String, Object>() {{
        			put("latitude", 60.186486380185436);        			
        			put("longtitude", 24.80741858482361);        			
        		}});
        		put("relative", new HashMap<String, Object>() {{
        			put("x", 0.8564);        			
        			put("y", 0.9876);        			
        		}});
        	}});
    		put("sensor", new HashMap<String, Object>() {{
        		put("temperature", new double[]{1.4, 5.5, 6.7, 8.9, 15.1});
        		put("hunidity", new double[]{1.4, 5.5, 6.7, 8.9, 15.1});
        	}});    		
    	}};
    	
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonString;
		try {
			jsonString = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
        return jsonString;
    }
}
