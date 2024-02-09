package com.ludo.study.studymatchingplatform.auth.kakao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.common.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.builder.AuthBuilder;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.AuthenticationResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;

	public AuthenticationResponse oauthLogin(final User user) {
		return makeAuthenticationResponse(user);
	}

	private AuthenticationResponse makeAuthenticationResponse(final User user) {
		final String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(user.getId()));
		return AuthBuilder.convertToAuthenticationResponse(accessToken);
	}

}
