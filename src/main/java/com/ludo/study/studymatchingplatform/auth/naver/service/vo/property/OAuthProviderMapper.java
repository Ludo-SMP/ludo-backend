package com.ludo.study.studymatchingplatform.auth.naver.service.vo.property;

import java.util.HashMap;
import java.util.Map;

public class OAuthProviderMapper {

	private OAuthProviderMapper() {
	}

	public static Map<String, OAuthProvider> mapBy(final OAuthProperties oAuthProperties) {
		Map<String, OAuthProvider> oauthProviders = new HashMap<>();

		oAuthProperties.getRegistration()
			.forEach(
				(social, registration) -> oauthProviders.put(
					social, new OAuthProvider(registration, oAuthProperties.getProvider().get(social)))
			);
		return oauthProviders;
	}
}
