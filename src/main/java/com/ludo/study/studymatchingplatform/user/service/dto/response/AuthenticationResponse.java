package com.ludo.study.studymatchingplatform.user.service.dto.response;

public record AuthenticationResponse(

		String refreshToken,
		String accessToken

) {
}
