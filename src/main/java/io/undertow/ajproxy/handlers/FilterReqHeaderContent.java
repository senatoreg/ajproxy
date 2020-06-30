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

import org.xnio.conduits.StreamSourceConduit;

public class FilterReqHeaderContent implements ConduitWrapper<StreamSourceConduit> {
    private static final Logger logger = LogManager.getLogger(FilterReqHeaderContent.class);

    FilterTransform[] requestTransform;

    public FilterReqHeaderContent() {
	ServerConfig serverConfig = ServerConfig.getInstance();

	if (serverConfig.getServer().getFilter() != null) {
	    if (serverConfig.getServer().getFilter().getRequest() != null &&
		serverConfig.getServer().getFilter().getRequest().getHeader() != null &&
		serverConfig.getServer().getFilter().getRequest().getHeader().getTransform() != null)
		this.requestTransform =  serverConfig.getServer()
		    .getFilter().getRequest().getHeader().getTransform();
	}

    }

    public FilterReqHeaderContent(FilterTransform[] requestTransform) {
	this.requestTransform = requestTransform;
    }

    @Override
    public StreamSourceConduit wrap(ConduitFactory<StreamSourceConduit> factory, HttpServerExchange exchange) {
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

	if (this.requestTransform == null)
		factory.create();

	logger.info("handling request content");
	for ( int i = 0 ; i < requestTransform.length ; i++  ) {
	    curHeader = requestTransform[i].getName();
	    try {
		headerMap = exchange.getRequestHeaders();
		headerValuesIter = headerMap.iterator();
		logger.info("request header count {}", headerMap.size());

		while (headerValuesIter.hasNext()) {
		    headerValues = headerValuesIter.next();
		    headerValueName = headerValues.getHeaderName().toString();
		    logger.trace("parsing request header {}", headerValueName);

		    if (!headerValueName.equals(curHeader))
			continue;

		    logger.trace("removing request header {}", headerValueName);
		    stringCollectionIter = headerMap.remove(curHeader).iterator();
		    while (stringCollectionIter.hasNext()) {
			s0 = stringCollectionIter.next();
			logger.trace("previous request header value {} for {}", s0, headerValueName);

			stringArrayList = new ArrayList<String>();
			if (requestTransform[i].getPattern() != null &&
			    requestTransform[i].getReplacement() != null) {
			    pattern = Pattern.compile(requestTransform[i].getPattern());
			    Matcher matcher = pattern.matcher(s0);
			    s = matcher.replaceAll(requestTransform[i].getReplacement());
			    logger.trace("new request value {} for {}", s, headerValueName);
			    if (s.length() > 0)
				stringArrayList.add(s);
			    else
				logger.debug("removing new empty request header value for {}", headerValueName);
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
