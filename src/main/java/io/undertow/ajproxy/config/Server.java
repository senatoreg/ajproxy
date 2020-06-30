package io.undertow.ajproxy.config;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private Listener[] listener;
    private Filter filter;

    public void setListener(Listener[] listener) {
	this.listener = listener;
	logger.debug("loaded {} listener(s)", listener.length);
    }

    public Listener[] getListener() {
	logger.trace("getting listeners");
	return this.listener;
    }

    public void setFilter(Filter filter) {
	this.filter = filter;
	logger.debug("loaded filter", filter);
    }

    public Filter getFilter() {
	logger.trace("getting filter");
	return this.filter;
    }
}
