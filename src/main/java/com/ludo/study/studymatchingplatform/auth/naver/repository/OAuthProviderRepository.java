package com.ludo.study.studymatchingplatform.auth.naver.repository;

import java.util.Map;

import com.ludo.study.studymatchingplatform.auth.naver.service.vo.property.OAuthProvider;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class OAuthProviderRepository {

	private final Map<String, OAuthProvider> providers;

	public String findClientId(final Social social) {
		return findByName(social.getName()).getClientId();
	}

	public String findClientSecret(final Social social) {
		return findByName(social.getName()).getClientSecret();
	}

	public String findLoginRedirectUri(final Social social) {
		return findByName(social.getName()).getLoginRedirectUri();
	}

	public String findSignupRedirectUri(final Social social) {
		return findByName(social.getName()).getSignupRedirectUri();
	}

	public String findAuthorizationUri(final Social social) {
		return findByName(social.getName()).getAuthorizationUri();
	}

	public String findTokenUri(final Social social) {
		return findByName(social.getName()).getTokenUri();
	}

	public String findUserInfoUri(final Social social) {
		return findByName(social.getName()).getUserInfoUri();
	}

	private OAuthProvider findByName(final String name) {
		return providers.get(name);
	}

}
