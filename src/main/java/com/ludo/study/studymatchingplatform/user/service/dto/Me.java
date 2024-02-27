package com.ludo.study.studymatchingplatform.user.service.dto;

import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Me {

	private final long id;

	private final String nickname;

	private final String email;

	public static Me from(final User user) {
		return new Me(user.getId(), user.getNickname(), user.getEmail());
	}
}
