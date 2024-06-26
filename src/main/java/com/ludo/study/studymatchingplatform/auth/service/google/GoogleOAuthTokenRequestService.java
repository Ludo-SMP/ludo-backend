package com.ludo.study.studymatchingplatform.auth.service.google;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleOAuthToken;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthTokenRequestService {

	private final RestTemplate restTemplate;
	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;

	public GoogleOAuthToken createOAuthToken(final String authorizationCode, final boolean isForSignup) {
		final String tokenUri = clientRegistrationAndProviderRepository.findTokenUri(Social.GOOGLE);
		final HttpHeaders headers = createHeaders();
		final MultiValueMap<String, String> body = createBody(authorizationCode, isForSignup);

		return requestOAuthToken(tokenUri, headers, body);
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		return headers;
	}

	private MultiValueMap<String, String> createBody(final String authorizationCode, final boolean isForSignup) {
		final String clientId = clientRegistrationAndProviderRepository.findClientId(Social.GOOGLE);
		final String clientSecret = clientRegistrationAndProviderRepository.findClientSecret(Social.GOOGLE);
		final String redirectUri =
				isForSignup ? clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.GOOGLE) :
						clientRegistrationAndProviderRepository.findLoginRedirectUri(Social.GOOGLE);
		final String grantType = clientRegistrationAndProviderRepository.findAuthorizationGrantType(Social.GOOGLE);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", grantType);
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("redirect_uri", redirectUri);
		body.add("code", authorizationCode);

		return body;
	}

	private GoogleOAuthToken requestOAuthToken(final String tokenUri, final HttpHeaders headers,
											   final MultiValueMap<String, String> body) {
		return restTemplate.postForObject(tokenUri, new HttpEntity<>(body, headers), GoogleOAuthToken.class);
	}

}
