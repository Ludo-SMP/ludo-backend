package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

public record ChangeUserNicknameResponse(
		UserResponse user
) {

	public record UserResponse(
			Long id, String nickname
	) {
	}

	public ChangeUserNicknameResponse(final User user) {
		this(new UserResponse(user.getId(), user.getNickname()));
	}
}
