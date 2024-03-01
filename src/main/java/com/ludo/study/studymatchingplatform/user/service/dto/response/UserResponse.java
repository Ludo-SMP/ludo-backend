package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.Builder;

@Builder
public record UserResponse(

		InnerUserResponse user

) {

	@Builder
	public record InnerUserResponse(

			Long id,
			String nickname,
			String email

	) {

		public static InnerUserResponse from(final User user) {
			return InnerUserResponse.builder()
					.id(user.getId())
					.nickname(user.getNickname())
					.email(user.getEmail())
					.build();
		}

	}

	public static UserResponse from(final User user) {
		final InnerUserResponse response = InnerUserResponse.from(user);
		return UserResponse.builder()
				.user(response)
				.build();
	}

}
