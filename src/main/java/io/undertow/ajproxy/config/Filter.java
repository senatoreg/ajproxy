package io.undertow.ajproxy.config;


public class Filter {
    private FilterField response;
    private FilterField request;
    
    public Filter() {
    }

    public void setResponse(FilterField response) {
	this.response = response;
    }

    public FilterField getResponse() {
	return this.response;
    }

    public void setRequest(FilterField request) {
	this.request = request;
    }

    public FilterField getRequest() {
	return this.request;
    }
}
