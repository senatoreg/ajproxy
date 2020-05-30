package io.undertow.ajproxy.config;

public class Listener {
    private String address;
    private int port;
    private int threads;
    private PrefixPath[] path;

    public void setAddress(String address) {
	this.address = address;
    }

    public String getAddress() {
	return this.address;
    }

    public void setPort(int port) {
	this.port = port;
    }

    public int getPort() {
	return this.port;
    }

    public void setThreads(int threads) {
	this.threads = threads;
    }

    public int getThreads() {
	return this.threads;
    }

    public void setPath(PrefixPath[] path) {
	this.path = path;
    }

    public PrefixPath[] getPath() {
	return this.path;
    }
}
