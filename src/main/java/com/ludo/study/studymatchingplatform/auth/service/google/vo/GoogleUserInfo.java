package com.ludo.study.studymatchingplatform.auth.service.google.vo;

import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleUserInfo {

	private final String id;
	private final String email;
	private final String name;

	public User toUser() {
		return User.builder()
				.social(Social.GOOGLE)
				.email(email)
				.nickname(name)
				.build();
	}
}
