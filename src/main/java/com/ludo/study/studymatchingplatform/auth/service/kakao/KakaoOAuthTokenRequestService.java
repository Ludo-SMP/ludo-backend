package com.ludo.study.studymatchingplatform.auth.service.kakao;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthTokenRequestService {

	private final RestTemplate restTemplate;
	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;

	public KakaoOAuthToken createOAuthToken(final String authorizationCode, final String redirectUri) {
		final String tokenUri = clientRegistrationAndProviderRepository.findTokenUri(Social.KAKAO);
		final HttpHeaders headers = createHeaders();
		final MultiValueMap<String, String> body = createBody(authorizationCode, redirectUri);
		return responseKakaoOAuthToken(tokenUri, headers, body);
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}

	private MultiValueMap<String, String> createBody(final String authorizationCode, final String redirectUri) {
		final String clientId = clientRegistrationAndProviderRepository.findClientId(Social.KAKAO);
		final String clientSecret = clientRegistrationAndProviderRepository.findClientSecret(Social.KAKAO);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("redirect_uri", redirectUri);
		body.add("code", authorizationCode);
		body.add("client_secret", clientSecret);
		return body;
	}

	private KakaoOAuthToken responseKakaoOAuthToken(final String tokenUri,
													final HttpHeaders headers,
													final MultiValueMap<String, String> body) {
		return restTemplate.postForObject(tokenUri, new HttpEntity<>(body, headers), KakaoOAuthToken.class);
	}

}
