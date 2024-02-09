package com.ludo.study.studymatchingplatform.auth.kakao.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface OauthNetworkService {

	<T> T makeSignupOauthUrl();

	<T> T makeLoginOauthUrl();

	<T> ResponseEntity<T> requestSignupToken(final Class<T> clazz, final Map<String, String> queryParams);

	<T> ResponseEntity<T> requestLoginToken(final Class<T> clazz, final Map<String, String> queryParams);

	<T> ResponseEntity<T> requestUserInfo(final Class<T> clazz, final Map<String, String> header);

}
