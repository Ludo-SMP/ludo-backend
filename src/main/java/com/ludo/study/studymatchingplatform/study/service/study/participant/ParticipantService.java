package com.ludo.study.studymatchingplatform.study.service.study.participant;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantService {

	private final ParticipantRepositoryImpl participantRepository;

	public void add(final Study study, final User user, final Position position, final Role role) {
		final LocalDateTime now = LocalDateTime.now();
		final Participant participant = Participant.from(study, user, position, role, now);
		study.addParticipant(participantRepository.save(participant));
	}

}
