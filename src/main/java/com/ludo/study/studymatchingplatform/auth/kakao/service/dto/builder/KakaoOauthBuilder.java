package com.ludo.study.studymatchingplatform.auth.kakao.service.dto.builder;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.KakaoOauthRedirectResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoOauthBuilder {

	public static KakaoOauthRedirectResponse convertToOauthRedirectResponse(final String url) {
		return new KakaoOauthRedirectResponse(url);
	}

}
