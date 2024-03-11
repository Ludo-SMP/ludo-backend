package com.ludo.study.studymatchingplatform.auth.service.kakao.dto.response;

public record KakaoSignUpResponse(

		boolean ok,
		String message,
		String accessToken

) {
}
