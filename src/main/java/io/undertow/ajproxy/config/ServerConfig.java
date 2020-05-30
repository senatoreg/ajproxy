package io.undertow.ajproxy.config;
 
import java.io.InputStream;
import java.io.FileInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
 
public class ServerConfig {
    private InputStream inputStream;

    private Server server;

    private static ServerConfig serverConfig;
    static {
	try {
	    serverConfig = new ServerConfig();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private ServerConfig() throws IOException {
 
	try {
	    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

	    String yamlFileName;

	    if ((yamlFileName = System.getProperty("serverConfigFile")) != null)
		inputStream = new FileInputStream(yamlFileName);
	    else {
		yamlFileName = "config/server.yaml";
		inputStream = getClass().getClassLoader().getResourceAsStream(yamlFileName);
	    }

	    if (inputStream != null) {
	        server = mapper.readValue(inputStream, Server.class);
	    } else {
		throw new FileNotFoundException("property file '" + yamlFileName + "' not found in the classpath");
	    }

	} catch (Exception e) {
	    System.out.println("Exception: " + e);
	} finally {
	    if (inputStream != null)
		inputStream.close();
	}
    }

    public static ServerConfig getInstance() {
	return serverConfig;
    }

    public Server getServer() {
	return this.server;
    }
}
