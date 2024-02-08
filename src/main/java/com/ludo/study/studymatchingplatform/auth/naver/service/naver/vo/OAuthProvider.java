package com.ludo.study.studymatchingplatform.auth.naver.service.naver.vo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OAuthProvider {

	private final String clientId;
	private final String clientSecret;
	private final String loginCallbackUrl;
	private final String signupCallbackUrl;

	private final String loginUrl;
	private final String tokenUrl;
	private final String userProfileUrl;

	public OAuthProvider(OAuthProperties.Registration registration, OAuthProperties.Provider provider) {
		this.clientId = registration.getClientId();
		this.clientSecret = registration.getClientSecret();
		this.loginCallbackUrl = registration.getLoginCallbackUrl();
		this.signupCallbackUrl = registration.getSignupCallbackUrl();

		this.loginUrl = provider.getLoginUrl();
		this.tokenUrl = provider.getTokenUrl();
		this.userProfileUrl = provider.getUserProfileUrl();
	}

}
