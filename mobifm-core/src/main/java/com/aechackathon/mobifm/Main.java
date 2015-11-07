package com.aechackathon.mobifm;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.aechackathon.mobifm.bim.BimObjectDetector;
import com.aechackathon.mobifm.proximi.ProximiAdapter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Main class.
 *
 */
public class Main {
	
    // Base URI the Grizzly HTTP server will listen on
//    public static final String BASE_URI = "http://localhost:8080/myapp/";
    
    public static final String CSV_FILE_PATH = "resources/Locations-Test.csv";
    
    public static final String CONFIG_FILE_PATH = "resources/config.properties";
    
    
    public static Properties configProperties;   
    
    
    
    

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.aechackathon.mobifm package
        final ResourceConfig rc = new ResourceConfig().packages("com.aechackathon.mobifm");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(getBaseUri()), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
//        final HttpServer server = startServer();
//        System.out.println(String.format("Jersey app started with WADL available at "
//                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
//        System.in.read();
//        server.stop();
    	
    	
    	try {
    		
    		ProximiAdapter proximi = new ProximiAdapter();
    		proximi.getData();
    		
//    		System.out.println("Working Directory = " + System.getProperty("user.dir"));
//    	
//	    	BimObjectDetector bimObjectDetector = new BimObjectDetector(CSV_FILE_PATH);
//	    	System.out.println(bimObjectDetector.getObject(50, 50));
	    	
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	}    	
    	
    }
    
    
    public static Properties getConfigProperties() {
    	if (configProperties == null) {
    		configProperties = new Properties();
    		try {
				configProperties.load(new FileReader(CONFIG_FILE_PATH));
			} catch (Exception e) {
				throw new RuntimeException("Loading config error: " + e.getMessage(), e);
			}
    	}
    	return configProperties;
    }
    
    public static String getConfigProperty(String name) {
    	String value = getConfigProperties().getProperty(name);
    	if (value == null) {
    		throw new IllegalArgumentException(String.format("Property %s is undefined", name));
    	}
    	return value;
    }
    
    
    public static String getBaseUri() {
    	return getConfigProperty("myapp.baseuri");
    }
}

