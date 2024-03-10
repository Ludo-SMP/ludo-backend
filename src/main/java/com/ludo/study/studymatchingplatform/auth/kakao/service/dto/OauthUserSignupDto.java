package com.ludo.study.studymatchingplatform.auth.kakao.service.dto;

import com.ludo.study.studymatchingplatform.user.domain.user.Social;

public record OauthUserSignupDto(

		Social social,
		String nickname,
		String email

) {

}
