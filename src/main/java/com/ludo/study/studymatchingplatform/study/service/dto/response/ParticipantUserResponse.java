package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Role;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.Builder;

@Builder
public record ParticipantUserResponse(

		Long id,
		String nickname,
		String email,
		Role role,
		PositionResponse position

) {

	public static ParticipantUserResponse from(final Participant participant) {
		final User user = participant.getUser();
		final PositionResponse response = PositionResponse.from(
				participant.getPosition());
		return ParticipantUserResponse.builder()
				.id(user.getId())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.role(participant.getRole())
				.position(response)
				.build();
	}

}
