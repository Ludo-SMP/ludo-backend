package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Role;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.user.domain.User;

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
