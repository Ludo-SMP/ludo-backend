package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.Builder;

@Builder
public record UserResponse(
		Long id,
		String nickname,
		String email
) {
	public static UserResponse from(final User user) {
		return UserResponse.builder()
				.id(user.getId())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.build();
	}

}
