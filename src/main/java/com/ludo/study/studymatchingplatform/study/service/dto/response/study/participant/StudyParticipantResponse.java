package com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant;

import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;

public record StudyParticipantResponse(long id, String name, String email, Role role) {

	public static StudyParticipantResponse from(final Participant participant) {
		return new StudyParticipantResponse(participant.getUser().getId(), participant.getUser().getNickname(),
				participant.getUser().getEmail(), participant.getRole());
	}

}
