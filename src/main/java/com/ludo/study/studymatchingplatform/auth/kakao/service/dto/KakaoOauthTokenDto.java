package com.ludo.study.studymatchingplatform.auth.kakao.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoOauthTokenDto(

		String accessToken,
		String refreshToken,
		String tokenType,
		Integer expiresIn

) {
}
