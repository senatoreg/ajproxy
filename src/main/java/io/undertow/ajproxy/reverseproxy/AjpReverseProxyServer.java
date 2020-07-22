package io.undertow.ajproxy.reverseproxy;

import java.net.URI;
import java.net.URISyntaxException;

import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.ssl.JsseXnioSsl;
import org.xnio.ssl.XnioSsl;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.ajproxy.config.ServerConfig;
import io.undertow.ajproxy.config.Server;
import io.undertow.ajproxy.config.Listener;
import io.undertow.ajproxy.config.PrefixPath;
import io.undertow.ajproxy.config.BalancedHost;
import io.undertow.ajproxy.handlers.AjpProxyHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AjpReverseProxyServer {
    private static final Logger logger = LogManager.getLogger(AjpReverseProxyServer.class);

    private static ServerConfig serverConfig;

    public static void main(final String[] args) {

        try {
	    serverConfig = ServerConfig.getInstance();
	    Server server = serverConfig.getServer();
	    AjpProxyHandler ajpProxyHandler;

	    for ( Listener listener : server.getListener() ) {
		PathHandler path = Handlers.path();

		    for ( PrefixPath prefixPath : listener.getPath() ) {
			Xnio xnio = Xnio.getInstance();

			OptionMap socketOptions = OptionMap.builder()
			    .set(Options.WORKER_IO_THREADS, prefixPath.getThreads())
			    .set(Options.TCP_NODELAY, true)
			    .set(Options.REUSE_ADDRESSES, true)
			    .getMap();

			XnioSsl jsseXnioSsl = new JsseXnioSsl(xnio, socketOptions);
	    
			LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient()
			    .setConnectionsPerThread(prefixPath.getConnections());

			for ( BalancedHost balancedHost : prefixPath.getHost() )
			    loadBalancer.addHost(new URI(balancedHost.getBalancedUri()),
						 null, jsseXnioSsl, socketOptions);

			ajpProxyHandler = new AjpProxyHandler(ProxyHandler.builder()
						.setProxyClient(loadBalancer)
						.setMaxRequestTime(prefixPath.getTimeout())
						.setRewriteHostHeader(true).build());

			path.addPrefixPath(prefixPath.getPrefix(), ajpProxyHandler);
		    }

		    final Undertow reverseProxy = Undertow.builder()
			.setServerOption(UndertowOptions.URL_CHARSET, "UTF-8")
			.addAjpListener(listener.getPort(), listener.getAddress())
			.setIoThreads(listener.getThreads())
			.setHandler(path)
			.build();
		    reverseProxy.start();
	    }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
