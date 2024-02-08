package com.ludo.study.studymatchingplatform.user.service.builder;

import com.ludo.study.studymatchingplatform.user.service.dto.response.AuthenticationResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthBuilder {

	public static AuthenticationResponse convertToAuthenticationResponse(
			final String refreshToken,
			final String accessToken) {
		return new AuthenticationResponse(refreshToken, accessToken);
	}

}
