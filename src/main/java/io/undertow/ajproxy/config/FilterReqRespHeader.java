package io.undertow.ajproxy.config;

public class FilterReqRespHeader {
    private FilterTransform[] transformList;
    
    public FilterReqRespHeader() {
    }

    public void setTransform(FilterTransform[] transformList) {
	this.transformList = transformList;
    }

    public FilterTransform[] getTransform() {
	return this.transformList;
    }
}
