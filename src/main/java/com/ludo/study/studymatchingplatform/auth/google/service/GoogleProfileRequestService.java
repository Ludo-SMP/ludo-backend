package com.ludo.study.studymatchingplatform.auth.google.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.google.GoogleUserInfo;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleProfileRequestService {

	private final RestTemplate restTemplate;
	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;

	public GoogleUserInfo createGoogleUserInfo(final String accessToken) {
		final String userInfoUri = clientRegistrationAndProviderRepository.findUserInfoUri(Social.GOOGLE);
		final HttpHeaders headers = createHeaders(accessToken);

		return requestUserProfile(userInfoUri, headers);
	}

	private HttpHeaders createHeaders(final String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		return headers;
	}

	private GoogleUserInfo requestUserProfile(final String userInfoUri, final HttpHeaders headers) {

		return restTemplate.exchange(
						userInfoUri,
						HttpMethod.GET,
						new HttpEntity<>(headers),
						GoogleUserInfo.class)
				.getBody();
	}

}
