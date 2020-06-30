package io.undertow.ajproxy.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrefixPath {
    private static final Logger logger = LogManager.getLogger(PrefixPath.class);

    private String prefix;
    private int conn;
    private int threads;
    private int timeout = 30000;
    private BalancedHost[] host;

    public void setPrefix(String prefix) {
	logger.debug("setting path prefix {}", prefix);
	this.prefix = prefix;
    }

    public String getPrefix() {
	logger.trace("getting path prefix {}", this.prefix);
	return this.prefix;
    }

    public int getThreads() {
	logger.trace("getting path threads {} for {}", this.threads, this.prefix);
	return this.threads;
    }

    public void setThreads(int threads) {
	logger.debug("setting path threads {} for {}", threads, this.prefix);
	this.threads = threads;
    }

    public void setConnections(int conn) {
	logger.debug("setting path connection(s) {} for {}", conn, this.prefix);
	this.conn = conn;
    }

    public int getConnections() {
	logger.trace("getting path connections {} for {}", this.conn, this.prefix);
	return this.conn;
    }

    public void setHost(BalancedHost[] host) {
	logger.debug("setting path host(s) {} for {}", host.length, this.prefix);
	this.host = host;
    }

    public BalancedHost[] getHost() {
	logger.trace("getting path host {} for {}", this.host.length, this.prefix);
	return this.host;
    }

    public void setTimeout(int timeout) {
	logger.debug("setting path timeout {} for {}", timeout, this.prefix);
	this.timeout = timeout * 1000;
    }

    public int getTimeout() {
	logger.trace("getting path timeout {} for {}", this.timeout, this.prefix);
	return this.timeout;
    }
}
