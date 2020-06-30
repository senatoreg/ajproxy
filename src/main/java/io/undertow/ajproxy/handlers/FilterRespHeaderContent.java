package io.undertow.ajproxy.handlers;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.undertow.server.ConduitWrapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.ConduitFactory;
import io.undertow.util.Headers;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import io.undertow.ajproxy.config.ServerConfig;
import io.undertow.ajproxy.config.FilterTransform;

import org.xnio.conduits.StreamSinkConduit;

public class FilterRespHeaderContent implements ConduitWrapper<StreamSinkConduit> {
    private static final Logger logger = LogManager.getLogger(FilterRespHeaderContent.class);
    
    FilterTransform[] responseTransform;

    public FilterRespHeaderContent() {
	ServerConfig serverConfig = ServerConfig.getInstance();

	if (serverConfig.getServer().getFilter() != null) {
	    if (serverConfig.getServer().getFilter().getResponse() != null &&
		serverConfig.getServer().getFilter().getResponse().getHeader() != null &&
		serverConfig.getServer().getFilter().getResponse().getHeader().getTransform() != null)
		this.responseTransform =  serverConfig.getServer()
		    .getFilter().getResponse().getHeader().getTransform();
	}

    }

    public FilterRespHeaderContent(FilterTransform[] responseTransform) {
	this.responseTransform = responseTransform;
    }

    @Override
    public StreamSinkConduit wrap(ConduitFactory<StreamSinkConduit> factory, HttpServerExchange exchange) {
	// TODO Auto-generated method stub
	HeaderMap headerMap;
	HeaderValues headerValues;
	Iterator<HeaderValues> headerValuesIter;
	Iterator<String> headerValueStringsIter;
	String headerValueString, headerValueName;
	String s, s0, curHeader;
	Iterator<String> stringCollectionIter;
	ArrayList<String> stringArrayList;
	Pattern pattern;

	if (this.responseTransform == null)
		factory.create();
	
	logger.info("handling request content");
	for ( int i = 0 ; i < responseTransform.length ; i++  ) {
	    curHeader = responseTransform[i].getName();
	    try {
		headerMap = exchange.getResponseHeaders();
		headerValuesIter = headerMap.iterator();
		logger.debug("response Header count {}", headerMap.size());

		while (headerValuesIter.hasNext()) {
		    headerValues = headerValuesIter.next();
		    headerValueName = headerValues.getHeaderName().toString();
		    logger.trace("parsing response header {}", headerValueName);
		    
		    if (!headerValueName.equals(curHeader))
			continue;

		    logger.trace("removing response header {}", headerValueName);
		    stringCollectionIter = headerMap.remove(curHeader).iterator();
		    while (stringCollectionIter.hasNext()) {
			s0 = stringCollectionIter.next();
			logger.trace("previous response header value {} for {}", s0, headerValueName);

			stringArrayList = new ArrayList<String>();
			if (responseTransform[i].getPattern() != null &&
			    responseTransform[i].getReplacement() != null) {
			    pattern = Pattern.compile(responseTransform[i].getPattern());
			    Matcher matcher = pattern.matcher(s0);
			    s = matcher.replaceAll(responseTransform[i].getReplacement());
			    logger.trace("new response header value {} for {}", s, headerValueName);
			    if (s.length() > 0)
				stringArrayList.add(s);
			    else
				logger.debug("removing new empty response header value for {}", headerValueName);
			}
			if (stringArrayList.size() > 0)
			    headerMap.addAll(new HttpString(curHeader), stringArrayList);
		    }
		}
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
	return factory.create();
    }
}
