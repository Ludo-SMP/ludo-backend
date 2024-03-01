package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class StudyParticipantResponse {

	private final long id;
	private final String name;
	private final String email;
	private final Role role;

	public static StudyParticipantResponse from(final Participant participant) {
		return new StudyParticipantResponse(participant.getUser().getId(), participant.getUser().getNickname(),
				participant.getUser().getEmail(), participant.getRole());
	}

}
