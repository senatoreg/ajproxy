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
import io.undertow.ajproxy.config.ServerConfig;
import io.undertow.ajproxy.config.Server;
import io.undertow.ajproxy.config.Listener;
import io.undertow.ajproxy.config.PrefixPath;
import io.undertow.ajproxy.config.BalancedHost;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

public class AjpReverseProxyServer {
    private static ServerConfig serverConfig;

    public static void main(final String[] args) {

        try {
	    serverConfig = ServerConfig.getInstance();
	    Server server = serverConfig.getServer();

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
			loadBalancer.addHost(new URI(balancedHost.getURI()), jsseXnioSsl);

		    path.addPrefixPath(prefixPath.getPrefix(),ProxyHandler.builder().setProxyClient(loadBalancer).setMaxRequestTime(prefixPath.getTimeout()).setRewriteHostHeader(true).build());
		}

		final Undertow reverseProxy = Undertow.builder()
		    .addAjpListener(listener.getPort(), listener.getAddress())
		    .setIoThreads(listener.getThreads())
		    .setHandler(path)
		    //.setHandler(ProxyHandler.builder().setProxyClient(loadBalancer).setMaxRequestTime(30000).setRewriteHostHeader(true).build())
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
