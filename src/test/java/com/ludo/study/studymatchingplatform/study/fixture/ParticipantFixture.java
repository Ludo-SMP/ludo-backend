package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class ParticipantFixture {

	public static Participant createParticipant(Study study, User user, Position position) {
		return Participant.builder()
				.study(study)
				.user(user)
				.position(position)
				.build();
	}

}
