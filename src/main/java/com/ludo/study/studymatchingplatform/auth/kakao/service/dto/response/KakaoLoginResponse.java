package com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response;

public record KakaoLoginResponse(

		String id,
		String nickname,
		String email,
		String assessToken

) {
}
