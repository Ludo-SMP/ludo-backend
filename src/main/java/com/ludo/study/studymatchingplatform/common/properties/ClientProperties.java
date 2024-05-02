package com.ludo.study.studymatchingplatform.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "client")
@RequiredArgsConstructor
@Getter
public class ClientProperties {

	private final String scheme;

	private final String domain;

	private final String url;

	public boolean isSecure() {
		return scheme.equals("https");
	}

}
