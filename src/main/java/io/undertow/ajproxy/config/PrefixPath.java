package io.undertow.ajproxy.config;

public class PrefixPath {
    private String prefix;
    private int conn;
    private int threads;
    private int timeout = 30000;
    private BalancedHost[] host;

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    public String getPrefix() {
	return this.prefix;
    }

    public int getThreads() {
	return this.threads;
    }

    public void setThreads(int threads) {
	this.threads = threads;
    }

    public void setConnections(int conn) {
	this.conn = conn;
    }

    public int getConnections() {
	return this.conn;
    }

    public void setHost(BalancedHost[] host) {
	this.host = host;
    }

    public BalancedHost[] getHost() {
	return this.host;
    }

    public void setTimeout(int timeout) {
	this.timeout = timeout * 1000;
    }

    public int getTimeout() {
	return this.timeout;
    }
}
