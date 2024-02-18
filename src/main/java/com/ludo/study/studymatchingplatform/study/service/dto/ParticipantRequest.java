package com.ludo.study.studymatchingplatform.study.service.dto;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.Builder;

@Builder
public record ParticipantRequest(

) {

	public Participant toParticipant(final User user, final Study study) {
		return Participant.builder()
				.user(user)
				.study(study)
				.build();
	}

}
