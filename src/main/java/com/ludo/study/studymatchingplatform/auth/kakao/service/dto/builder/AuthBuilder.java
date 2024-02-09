package com.ludo.study.studymatchingplatform.auth.kakao.service.dto.builder;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.AuthenticationResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthBuilder {

	public static AuthenticationResponse convertToAuthenticationResponse(
			final String accessToken) {
		return new AuthenticationResponse(accessToken);
	}

}
