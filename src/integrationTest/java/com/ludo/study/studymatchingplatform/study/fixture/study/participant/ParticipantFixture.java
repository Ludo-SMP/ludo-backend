package com.ludo.study.studymatchingplatform.study.fixture.study.participant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public class ParticipantFixture {

	public static Participant createParticipant(Study study, User user, Position position, Role role) {
		return Participant.builder()
				.study(study)
				.user(user)
				.role(role)
				.position(position)
				.build();
	}

}
