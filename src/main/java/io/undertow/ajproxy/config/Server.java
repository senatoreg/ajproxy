package io.undertow.ajproxy.config;

public class Server {
    private Listener[] listener;

    public void setListener(Listener[] listener) {
	this.listener = listener;
    }

    public Listener[] getListener() {
	return this.listener;
    }
}
