package com.ludo.study.studymatchingplatform.auth.kakao.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;

@JsonNaming(value = PropertyNamingStrategies.LowerCaseStrategy.class)
public record KakaoUserProfileDto(

		KakaoUserPropertiesDto properties,
		KakaoUserAccountDto kakao_account

) {

	public String getEmail() {
		return kakao_account.email();
	}

	public User toUser() {
		return User.builder()
				.social(Social.NAVER)
				.nickname(properties.nickname())
				.email(kakao_account.email())
				.build();
	}

}
