package com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response;

public record AuthenticationResponse(

		String refreshToken,
		String accessToken

) {
}
