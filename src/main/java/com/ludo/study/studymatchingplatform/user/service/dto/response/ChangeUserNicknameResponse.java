package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

public record ChangeUserNicknameResponse(
		Long id, String nickname
) {
	public ChangeUserNicknameResponse(final User user) {
		this(user.getId(), user.getNickname());
	}
}
