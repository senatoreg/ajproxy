package io.undertow.ajproxy.config;

import java.lang.Boolean;

public class BalancedHost {
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

    public String getURI() {
	String proto = this.ssl ? "https" : "http";

	return proto + "://" + this.address + ":" + this.port;
    }
}
