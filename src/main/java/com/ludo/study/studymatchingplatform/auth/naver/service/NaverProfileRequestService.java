package com.ludo.study.studymatchingplatform.auth.naver.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.naver.repository.OAuthProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.response.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.response.UserProfile;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverProfileRequestService {

	private final RestTemplate restTemplate;
	private final OAuthProviderRepository oAuthProviderRepository;

	public UserProfile createNaverProfile(final NaverOAuthToken naverOAuthToken) {
		final String userInfoUri = oAuthProviderRepository.findUserInfoUri(Social.NAVER);
		final HttpHeaders headers = createHeaders(naverOAuthToken);

		return requestUserProfile(userInfoUri, headers);
	}

	private HttpHeaders createHeaders(final NaverOAuthToken naverOAuthToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(naverOAuthToken.getAccessToken());

		return headers;
	}

	private UserProfile requestUserProfile(final String userInfoUri, final HttpHeaders headers) {

		return restTemplate.exchange(
				userInfoUri,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				UserProfile.class)
			.getBody();
	}

}
