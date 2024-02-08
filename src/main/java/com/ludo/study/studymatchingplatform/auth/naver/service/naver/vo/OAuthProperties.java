package com.ludo.study.studymatchingplatform.auth.naver.service.naver.vo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@ConfigurationProperties(prefix = "oauth2")
public class OAuthProperties {

	private final Map<String, Registration> registration = new HashMap<>();
	private final Map<String, Provider> provider = new HashMap<>();

	@Setter
	@Getter
	@ToString
	public static class Registration {
		private String clientId;
		private String clientSecret;
		private String loginCallbackUrl;
		private String signupCallbackUrl;
	}

	@Setter
	@Getter
	@ToString
	public static class Provider {
		private String loginUrl;
		private String tokenUrl;
		private String userProfileUrl;
	}

}
