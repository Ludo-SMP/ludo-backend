package com.ludo.study.studymatchingplatform.study.repository;

import static com.ludo.study.studymatchingplatform.study.domain.QParticipant.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ParticipantRepositoryImpl {

	private final ParticipantJpaRepository participantJpaRepository;
	private final JPAQueryFactory q;

	public Participant save(final Participant participant) {
		return participantJpaRepository.save(participant);
	}

	public Optional<Participant> find(final Long studyId, final Long userId) {
		return Optional.ofNullable(q.selectFrom(participant)
				.where(participant.study.id.eq(studyId))
				.where(participant.user.id.eq(userId))
				.fetchOne()
		);
	}

}
