package com.ludo.study.studymatchingplatform.auth.kakao.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoProfileRequestService {

	private final RestTemplate restTemplate;
	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;

	public KakaoUserProfileDto createKakaoProfile(final KakaoOAuthToken kakaoOAuthToken) {
		final String userInfoUri = clientRegistrationAndProviderRepository.findUserInfoUri(Social.KAKAO);
		final HttpHeaders headers = createHeaders(kakaoOAuthToken);
		return requestUserProfile(userInfoUri, headers);
	}

	private HttpHeaders createHeaders(final KakaoOAuthToken kakaoOAuthToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(kakaoOAuthToken.getAccessToken());
		return headers;
	}

	private KakaoUserProfileDto requestUserProfile(final String userInfoUri, final HttpHeaders headers) {
		return restTemplate.exchange(
						userInfoUri,
						HttpMethod.GET,
						new HttpEntity<>(headers),
						KakaoUserProfileDto.class)
				.getBody();
	}

}
