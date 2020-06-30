package io.undertow.ajproxy.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.undertow.server.HttpHandler;
//import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import java.util.Iterator;
import io.undertow.ajproxy.config.ServerConfig;
import io.undertow.ajproxy.config.FilterTransform;

public class AjpProxyHandler implements HttpHandler {
    private static final Logger logger = LogManager.getLogger(AjpProxyHandler.class);

    private ProxyHandler proxyHandler;

    public AjpProxyHandler(ProxyHandler proxyHandler) {
	this.proxyHandler = proxyHandler;
    }

    public void handleRequest(HttpServerExchange exchange) {
	ServerConfig serverConfig = ServerConfig.getInstance();
	FilterTransform[] responseTransform;
	FilterTransform[] requestTransform;

	try {
	    if (serverConfig.getServer().getFilter() != null) {
		if (serverConfig.getServer().getFilter().getResponse() != null &&
		    serverConfig.getServer().getFilter().getResponse().getHeader() != null &&
		    serverConfig.getServer().getFilter().getResponse().getHeader().getTransform() != null) {

		    responseTransform =  serverConfig.getServer()
			.getFilter().getResponse().getHeader().getTransform();
			
		    exchange
			.addResponseWrapper(new FilterRespHeaderContent(responseTransform));
		}

		if (serverConfig.getServer().getFilter().getRequest() != null &&
		    serverConfig.getServer().getFilter().getRequest().getHeader() != null &&
		    serverConfig.getServer().getFilter().getRequest().getHeader().getTransform() != null) {

		    requestTransform =  serverConfig.getServer()
			.getFilter().getRequest().getHeader().getTransform();

		    logger.debug("Adding Request Wrapper");
		    exchange
			.addRequestWrapper(new FilterReqHeaderContent(requestTransform));
		}

		this.proxyHandler.handleRequest(exchange);
	}
	
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
