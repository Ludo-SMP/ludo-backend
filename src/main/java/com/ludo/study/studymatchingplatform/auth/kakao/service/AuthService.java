package com.ludo.study.studymatchingplatform.auth.kakao.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.common.TokenProvider;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.builder.AuthBuilder;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.AuthenticationResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final TokenProvider tokenProvider;

	public AuthenticationResponse oauthLogin(final User user) {
		return makeAuthenticationResponse(user);
	}

	private AuthenticationResponse makeAuthenticationResponse(final User user) {
		final String refreshToken = tokenProvider.createRefreshToken(user.getEmail(), Map.of());
		final String accessToken = tokenProvider.createAccessToken(user.getEmail(), Map.of());
		return AuthBuilder.convertToAuthenticationResponse(refreshToken, accessToken);
	}

	public boolean isCertified(final String token) {
		return tokenProvider.isValidToken(token);
	}

	public String findIdentifierByToken(final String token) {
		return tokenProvider.findSubject(token);
	}

}
