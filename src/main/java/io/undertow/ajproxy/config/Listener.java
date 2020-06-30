package io.undertow.ajproxy.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Listener {
    private static final Logger logger = LogManager.getLogger(Listener.class);

    private String address;
    private int port;
    private int threads;
    private PrefixPath[] path;

    public void setAddress(String address) {
	logger.debug("setting listener address {}", address);
	this.address = address;
    }

    public String getAddress() {
	return this.address;
    }

    public void setPort(int port) {
	logger.debug("setting listener port {}", port);
	this.port = port;
    }

    public int getPort() {
	return this.port;
    }

    public void setThreads(int threads) {
	logger.debug("setting listener threads {}", threads);
	this.threads = threads;
    }

    public int getThreads() {
	return this.threads;
    }

    public void setPath(PrefixPath[] path) {
	logger.debug("loading {} listener(s) for {}:{}", path.length, this.address, this.port);
	this.path = path;
    }

    public PrefixPath[] getPath() {
	return this.path;
    }
}
