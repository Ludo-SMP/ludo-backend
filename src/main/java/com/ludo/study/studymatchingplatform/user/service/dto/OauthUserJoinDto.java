package com.ludo.study.studymatchingplatform.user.service.dto;

import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.Builder;

@Builder
public record OauthUserJoinDto(

		Social social,
		String nickname,
		String email,
		String refreshToken,
		String accessToken

) {

}
