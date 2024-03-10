package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant.ParticipantUserResponse;

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
