package com.ludo.study.studymatchingplatform.auth.naver.service.vo.property;

import java.util.HashMap;
import java.util.Map;

public class ClientRegistrationAndProviderMapper {

	private ClientRegistrationAndProviderMapper() {
	}

	public static Map<String, ClientRegistrationAndProvider> mapBy(final OAuthProperties oAuthProperties) {
		Map<String, ClientRegistrationAndProvider> clientRegistrationAndProviders = new HashMap<>();

		oAuthProperties.getRegistration()
				.forEach((social, registration) -> clientRegistrationAndProviders.put(
						social, new ClientRegistrationAndProvider(registration,
								oAuthProperties.getProvider().get(social)))
				);
		return clientRegistrationAndProviders;
	}
}
