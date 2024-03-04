package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.Participant;

import lombok.Builder;

@Builder
public record ApplyAcceptResponse(

		ParticipantUserResponse participant

) {

	public static ApplyAcceptResponse from(final Participant participant) {
		final ParticipantUserResponse response = ParticipantUserResponse.from(participant);
		return ApplyAcceptResponse.builder()
				.participant(response)
				.build();
	}

}
