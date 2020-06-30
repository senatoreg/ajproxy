package io.undertow.ajproxy.config;


public class FilterField {
    private FilterReqRespHeader header;
    
    public FilterField() {
    }

    public void setHeader(FilterReqRespHeader header) {
	this.header = header;
    }

    public FilterReqRespHeader getHeader() {
	return this.header;
    }
}
