package com.ludo.study.studymatchingplatform.auth.google;

import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

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
