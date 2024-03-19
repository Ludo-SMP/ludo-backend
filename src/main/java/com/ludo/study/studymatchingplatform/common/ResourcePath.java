package com.ludo.study.studymatchingplatform.common;

public enum ResourcePath {

	STACK_IMAGE("/api/static/stack/images/", "/api/static/stack/images/**", "classpath:/api/static/stack/images/");

	private final String path;
	private final String pathPattern;
	private final String pathWithClassPath;

	ResourcePath(String path, String pathPattern, String pathWithClassPath) {
		this.path = path;
		this.pathPattern = pathPattern;
		this.pathWithClassPath = pathWithClassPath;
	}

	public String getPath() {
		return path;
	}

	public String getPathPattern() {
		return pathPattern;
	}

	public String getPathWithClassPath() {
		return pathWithClassPath;
	}
}
