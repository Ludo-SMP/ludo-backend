package com.ludo.study.studymatchingplatform.study.fixture.study.participant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;

public class ParticipantFixture {

	public static final Participant study1ParticipantUser1 = Participant.builder()
			.study(StudyFixture.study1)
			.user(UserFixture.user1)
			.role(Role.OWNER)
			.build();

	public static final Participant study1ParticipantUser2 = Participant.builder()
			.study(StudyFixture.study1)
			.user(UserFixture.user2)
			.role(Role.MEMBER)
			.build();

	public static final Participant study1ParticipantUser3 = Participant.builder()
			.study(StudyFixture.study1)
			.user(UserFixture.user3)
			.role(Role.MEMBER)
			.build();

	public static Participant createParticipant(Study study, User user, Position position, Role role) {
		return Participant.builder()
				.study(study)
				.user(user)
				.role(role)
				.position(position)
				.build();
	}

}
