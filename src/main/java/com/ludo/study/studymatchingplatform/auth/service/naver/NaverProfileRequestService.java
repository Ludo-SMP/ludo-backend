package com.ludo.study.studymatchingplatform.auth.service.naver;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverUserProfile;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverProfileRequestService {

	private final RestTemplate restTemplate;
	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;

	public NaverUserProfile createNaverProfile(final NaverOAuthToken naverOAuthToken) {
		final String userInfoUri = clientRegistrationAndProviderRepository.findUserInfoUri(Social.NAVER);
		final HttpHeaders headers = createHeaders(naverOAuthToken);

		return requestUserProfile(userInfoUri, headers);
	}

	private HttpHeaders createHeaders(final NaverOAuthToken naverOAuthToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(naverOAuthToken.getAccessToken());

		return headers;
	}

	private NaverUserProfile requestUserProfile(final String userInfoUri, final HttpHeaders headers) {

		return restTemplate.exchange(
						userInfoUri,
						HttpMethod.GET,
						new HttpEntity<>(headers),
						NaverUserProfile.class)
				.getBody();
	}

}
