package io.undertow.ajproxy.config;

import java.lang.Boolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BalancedHost {
    private static final Logger logger = LogManager.getLogger(BalancedHost.class);

    private String uri;
    private String context;
    private String address;
    private int port;
    private Boolean ssl;

    public Boolean getSsl() {
	return this.ssl;
    }

    public void setSsl(Boolean ssl) {
	this.ssl = ssl;
    }

    public int getPort() {
	return this.port;
    }

    public void setPort(int port) {
	this.port = port;
    }

    public String getAddress() {
	return this.address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getUri() {
	return this.uri;
    }

    public void setUri(String uri) {
	this.uri = uri;
    }

    public String getContext() {
	return this.context;
    }

    public void setcontext(String context) {
	this.context = context;
    }

    public String getBalancedUri() {
	if (this.uri != null)
	    return this.uri;

	String proto = this.ssl ? "https" : "http";
	String prefix = this.context != null ? this.context : "";
	return proto + "://" + this.address + ":" + this.port + prefix;
    }
}
