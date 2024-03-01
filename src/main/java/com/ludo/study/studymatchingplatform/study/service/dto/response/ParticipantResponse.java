package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

import lombok.Builder;

@Builder
public record ParticipantResponse(

		UserResponse participant

) {

	public static ParticipantResponse fromWithOutStudyResponse(final Participant participant) {
		final UserResponse userResponse = UserResponse.from(participant.getUser());
		return ParticipantResponse.builder()
				.participant(userResponse)
				.build();
	}

}
