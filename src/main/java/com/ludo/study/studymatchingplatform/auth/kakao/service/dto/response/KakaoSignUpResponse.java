package com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response;

public record KakaoSignUpResponse(

		boolean ok,
		String message,
		String accessToken

) {
}
