package com.ludo.study.studymatchingplatform.auth.kakao.service;

import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.builder.KakaoOauthBuilder;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.KakaoOauthRedirectResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOauthNetwordService implements OauthNetworkService {

	private static final String CALLBACK_URL_PROPERTY = "oauth2.client.provider.kakao.authorization-uri";
	private static final String RESPONSE_TYPE_PROPERTY = "oauth2.client.registration.kakao.authorization-grant-type";
	private static final String CLIENT_ID_PROPERTY = "oauth2.client.registration.kakao.client-id";
	private static final String CLIENT_SECRET_PROPERTY = "oauth2.client.registration.kakao.client-secret";
	private static final String TOKEN_URL_PROPERTY = "oauth2.client.provider.kakao.token-uri";
	private static final String SIGNUP_REDIRECT_URL_PROPERTY = "oauth2.client.registration.kakao.signup-redirect-uri";
	private static final String LOGIN_REDIRECT_URL_PROPERTY = "oauth2.client.registration.kakao.login-redirect-uri";
	private static final String USER_INFO_URL_PROPERTY = "oauth2.client.provider.kakao.user-info-uri";

	private final RestTemplate restTemplate;
	private final Environment environment;

	@Override
	public KakaoOauthRedirectResponse makeSignupOauthUrl() {
		final String callbackUrl = findProperty(CALLBACK_URL_PROPERTY);
		final String responseType = findProperty(RESPONSE_TYPE_PROPERTY);
		final String clientId = findProperty(CLIENT_ID_PROPERTY);
		final String redirectUrl = findProperty(SIGNUP_REDIRECT_URL_PROPERTY);
		final String url = String.format(callbackUrl, responseType, clientId, redirectUrl);
		return KakaoOauthBuilder.convertToOauthRedirectResponse(url);
	}

	@Override
	public KakaoOauthRedirectResponse makeLoginOauthUrl() {
		final String callbackUrl = findProperty(CALLBACK_URL_PROPERTY);
		final String responseType = findProperty(RESPONSE_TYPE_PROPERTY);
		final String clientId = findProperty(CLIENT_ID_PROPERTY);
		final String redirectUrl = findProperty(LOGIN_REDIRECT_URL_PROPERTY);
		final String url = String.format(callbackUrl, responseType, clientId, redirectUrl);
		return KakaoOauthBuilder.convertToOauthRedirectResponse(url);
	}

	@Override
	public <T> ResponseEntity<T> requestSignupToken(final Class<T> clazz, final Map<String, String> queryParams) {
		final String url = String.format(findProperty(TOKEN_URL_PROPERTY),
				findProperty(CLIENT_ID_PROPERTY), findProperty(SIGNUP_REDIRECT_URL_PROPERTY),
				queryParams.get("code"), findProperty(CLIENT_SECRET_PROPERTY));
		return restTemplate.getForEntity(url, clazz);
	}

	@Override
	public <T> ResponseEntity<T> requestLoginToken(final Class<T> clazz, final Map<String, String> queryParams) {
		final String url = String.format(findProperty(TOKEN_URL_PROPERTY),
				findProperty(CLIENT_ID_PROPERTY), findProperty(LOGIN_REDIRECT_URL_PROPERTY),
				queryParams.get("code"), findProperty(CLIENT_SECRET_PROPERTY));
		return restTemplate.getForEntity(url, clazz);
	}

	@Override
	public <T> ResponseEntity<T> requestUserInfo(final Class<T> clazz, final Map<String, String> headers) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		for (final Map.Entry<String, String> entry : headers.entrySet()) {
			httpHeaders.set(entry.getKey(), entry.getValue());
		}
		final HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
		return restTemplate.exchange(findProperty(USER_INFO_URL_PROPERTY), HttpMethod.GET, httpEntity, clazz);
	}

	private String findProperty(final String property) {
		return environment.getProperty(property);
	}

}
