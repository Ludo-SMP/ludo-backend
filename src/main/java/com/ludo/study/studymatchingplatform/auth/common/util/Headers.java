package com.ludo.study.studymatchingplatform.auth.common.util;

public enum Headers {

	USER_AGENT("User-Agent"),
	X_FORWARDED_FOR("X-Forwarded-For"),
	X_REAL_IP("x-real-ip"),
	X_ORIGINAL_FORWARDED_FOR("x-original-forwarded-for"),
	PROXY_CLIENT_IP("Proxy-Client-IP"),
	WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
	HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR"),
	HTTP_X_FORWARDED("HTTP_X_FORWARDED"),
	HTTP_X_CLUSTER_CLIENT_IP("HTTP_X_CLUSTER_CLIENT_IP"),
	HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
	HTTP_FORWARDED_FOR("HTTP_FORWARDED_FOR"),
	HTTP_FORWARDED("HTTP_FORWARDED"),
	HTTP_VIA("HTTP_VIA"),
	REMOTE_ADDR("REMOTE_ADDR"),
	UNKNOWN("unknown");

	private final String header;

	Headers(final String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

}
