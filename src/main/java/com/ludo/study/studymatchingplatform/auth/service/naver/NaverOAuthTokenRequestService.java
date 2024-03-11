package com.ludo.study.studymatchingplatform.auth.service.naver;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOAuthTokenRequestService {

	private final RestTemplate restTemplate;
	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;

	public NaverOAuthToken createOAuthToken(final String authorizationCode) {
		final String tokenUri = clientRegistrationAndProviderRepository.findTokenUri(Social.NAVER);
		final HttpHeaders headers = createHeaders();
		final MultiValueMap<String, String> body = createBody(authorizationCode);

		return requestOAuthToken(tokenUri, headers, body);
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		return headers;
	}

	private MultiValueMap<String, String> createBody(final String authorizationCode) {
		final String clientId = clientRegistrationAndProviderRepository.findClientId(Social.NAVER);
		final String clientSecret = clientRegistrationAndProviderRepository.findClientSecret(Social.NAVER);
		final String grantType = clientRegistrationAndProviderRepository.findAuthorizationGrantType(Social.NAVER);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", grantType);
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("code", authorizationCode);

		return body;
	}

	private NaverOAuthToken requestOAuthToken(
			final String tokenUri,
			final HttpHeaders headers,
			final MultiValueMap<String, String> body
	) {
		return restTemplate.postForObject(tokenUri, new HttpEntity<>(body, headers), NaverOAuthToken.class);
	}

}
