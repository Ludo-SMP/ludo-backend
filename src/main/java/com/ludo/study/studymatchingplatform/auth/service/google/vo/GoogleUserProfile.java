package com.ludo.study.studymatchingplatform.auth.service.google.vo;

import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleUserProfile {

	private final String id;
	private final String email;

	public User toUser() {
		return User.builder()
				.social(Social.GOOGLE)
				.email(email)
				.build();
	}
}
