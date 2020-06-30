package io.undertow.ajproxy.config;

public class FilterTransform {
    private String pattern;
    private String name;
    private String replacement;
    
    public FilterTransform() {
    }

    public void setPattern(String pattern) {
	this.pattern = pattern;
    }

    public String getPattern() {
	return this.pattern;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return this.name;
    }

    public void setReplacement(String replacement) {
	this.replacement = replacement;
    }

    public String getReplacement() {
	return this.replacement;
    }
}
