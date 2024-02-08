package com.ludo.study.studymatchingplatform.auth.naver.service.naver.vo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OAuthProvider {

	private final String clientId;
	private final String clientSecret;
	private final String loginRedirectUri;
	private final String signupRedirectUri;

	private final String authorizationUri;
	private final String tokenUri;
	private final String userInfoUri;

	public OAuthProvider(OAuthProperties.Registration registration, OAuthProperties.Provider provider) {
		this.clientId = registration.getClientId();
		this.clientSecret = registration.getClientSecret();
		this.loginRedirectUri = registration.getLoginRedirectUri();
		this.signupRedirectUri = registration.getSignupRedirectUri();

		this.authorizationUri = provider.getAuthorizationUri();
		this.tokenUri = provider.getTokenUri();
		this.userInfoUri = provider.getUserInfoUri();
	}

}
