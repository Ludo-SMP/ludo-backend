package com.ludo.study.studymatchingplatform.auth.service.kakao.dto.response;

public record KakaoLoginResponse(

		String id,
		String nickname,
		String email,
		String assessToken

) {
}
