package com.ludo.study.studymatchingplatform.auth.service.dto;

public record KakaoOAuthTokenTestResponse(

		String tokenType,

		String accessToken,

		String refreshToken,

		Integer expiresIn

) {

}
