package com.ludo.study.studymatchingplatform.user.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.service.builder.AuthBuilder;
import com.ludo.study.studymatchingplatform.user.service.dto.response.AuthenticationResponse;

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
		final String accessToken = tokenProvider.createRefreshToken(user.getEmail(), Map.of());
		return AuthBuilder.convertToAuthenticationResponse(refreshToken, accessToken);
	}

}
