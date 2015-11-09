package com.aechackathon.mobifm;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aechackathon.mobifm.data.Department;
import com.aechackathon.mobifm.data.Device;
import com.aechackathon.mobifm.proximity.ProximityAdapter;
import com.aechackathon.mobifm.thingsee.ThingseeAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tracing.dtrace.DependencyClass;

/**
 * Root resource (exposed at "data" path)
 */
@Path("data")
public class DataResource {
	
	private static final Object MUTEX = new Object();
	private static final Object WAIT = new Object();
	private static boolean initialized;
	private static boolean threadStarted;
	
	public static boolean isRunning;
	
//	private static Map<?, ?> device;
//	private static String deviceName;
//	private static String deviceId;
//	private static Map<?, ?> department;
//	private static String departmentId;
//	private static String departmentName;
	
	private static Device device;
	private static Department department;
	
	private static List<Double> temperatures = new LinkedList<>();
	private static List<Double> humidities = new LinkedList<>();
	private static List<Double> pressures = new LinkedList<>();
	private static List<Double> luminances = new LinkedList<>();
	
	public static void init() {
		
		if (!initialized) {
			
			synchronized (MUTEX) {
				
				try {
				
		    		ProximityAdapter proximity = new ProximityAdapter();

		    		System.out.println("Devices:");
		    		Map<String, Object> devices = proximity.getDeviceByName(Main.getConfigProperty("proximity.device.name"));		    		
		    		System.out.println(devices);
		    		
		    		device = new Device(devices);
		    		
	    			String deviceId = device.getId();
	        		System.out.println(device);

	        		String departmentId = device.getDepartmentId();
	        	    		
		    		System.out.println("Departments:");
		    		Map<String, Object> departments = proximity.getDepartmentById(departmentId);		    		
		    		department = new Department(departments);		    		
	        		String departmentName = department.getName();	        		
	        		System.out.println(department);
//		    		
//		    		System.out.println("AnonymousVisitors");
//		    		System.out.println(proximity.getAnonymousVisitors());
//		    		
//	
//		    		ThingseeAdapter thingsee = new ThingseeAdapter();
//		    		Map<String, Double> thingseeReadings = thingsee.getThingseeData();
//		    		System.out.println("Temperature: " + thingseeReadings.get("temperature") + 
//		    				" Humidity: " + thingseeReadings.get("humidity") +
//		    				" Pressure: " + thingseeReadings.get("pressure") +
//		    				" Luminance: " + thingseeReadings.get("luminance") );
				
					startThread();

				} catch (Exception e) {
					throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
				}
					
				initialized = true;
				
			}
			
		}
		
	}
		
		
	private static void startThread() {
		
		if (threadStarted) {
			return;
		}
		System.out.println("Starting thread");
		
		new Thread("New Thread") {
		      public void run(){
		    	  
		    	  isRunning = true;
		    	  
		    	  while(isRunning) {
		    	  
			  		ThingseeAdapter thingsee = new ThingseeAdapter();
					Map<String, Double> thingseeReadings;
					try {
						thingseeReadings = thingsee.getThingseeData();
					} catch (IOException e1) {
						throw new RuntimeException("Unexpected error: " + e1.getMessage(), e1);
					}
					
					double temperature = thingseeReadings.get("temperature");
					double humidity = thingseeReadings.get("humidity");
					double pressure = thingseeReadings.get("pressure");
					double luminance = thingseeReadings.get("luminance");
					
					temperatures.add(temperature);
					humidities.add(humidity);
					pressures.add(pressure);
					luminances.add(luminance);
					
					int max = 20;
					
					if (temperatures.size() > max) {
						temperatures.remove(0);
						humidities.remove(0);
						pressures.remove(0);
						luminances.remove(0);
					}
					
    				try {
						sleep(1000);
					} catch (InterruptedException e) {
						return;
					}
					
		    	  }
				
		      }
		 }.start();
		 
		 threadStarted = true;
		 
		System.out.println("Thread started");
	}
	
	
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getData() {
    	System.out.println("Got request");
    	return getRuntimeData();
    }
	
	
    @SuppressWarnings({ "serial", "unused" })
    private static String getRuntimeData() {
//		ThingseeAdapter thingsee = new ThingseeAdapter();
//		Map<String, Double> thingseeReadings;
//		try {
//			thingseeReadings = thingsee.getThingseeData();
//		} catch (IOException e1) {
//			throw new RuntimeException("Unexpected error: " + e1.getMessage(), e1);
//		}
//		
//		double temperature = thingseeReadings.get("temperature");
//		double humidity = thingseeReadings.get("humidity");
//		double pressure = thingseeReadings.get("pressure");
//		double luminance = thingseeReadings.get("luminance");
//		System.out.println("Temperature: " + temperature + 
//				" Humidity: " + humidity +
//				" Pressure: " + pressure +
//				" Luminance: " + luminance);
    	
    	int last = temperatures.size() - 1;
    	
    	System.out.println("Last=" + last);
    	
    	Map<String, Object> data = new HashMap<String, Object>() {{
    		put("location", new HashMap<String, Object>() {{
        		put("name", department.getName());
        		put("guid", "2iJMN5fwTCXRXnQXOq5A8l");
        		put("absolute", new HashMap<String, Object>() {{
        			put("latitude", device.getLatitude());        			
        			put("longtitude", device.getLongtitude());        			
        		}});
        		put("relative", new HashMap<String, Object>() {{
        			put("x", 0.8564);        			
        			put("y", 0.9876);        			
        		}});
        	}});
    		put("sensor", new HashMap<String, Object>() {{
        		put("temperature", new double[]{temperatures.get(last)});
        		put("hunidity", new double[]{humidities.get(last)});
        		put("presure", new double[]{pressures.get(last)});
        		put("luminance", new double[]{luminances.get(last)});
        	}});    		
    		put("sensorHistory", new HashMap<String, Object>() {{
        		put("temperature", temperatures.toArray());
        		put("hunidity", humidities.toArray());
        		put("presure", pressures.toArray());
        		put("luminance", luminances.toArray());
        	}});    		
    	}};
    	
    	System.out.println("2");

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
	
    @SuppressWarnings({ "serial", "unused" })
    private static String getTestData() {
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
        		put("luminance", new double[]{10, 20, 500, 610, 670});
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
