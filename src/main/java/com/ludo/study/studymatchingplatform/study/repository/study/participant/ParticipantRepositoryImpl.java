package com.ludo.study.studymatchingplatform.study.repository.study.participant;

import static com.ludo.study.studymatchingplatform.study.domain.study.participant.QParticipant.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ParticipantRepositoryImpl {

	private final JPAQueryFactory q;

	private final ParticipantJpaRepository participantJpaRepository;

	public Participant save(final Participant participant) {
		return participantJpaRepository.save(participant);
	}

	public Optional<Participant> find(final Long studyId, final Long userId) {
		return Optional.ofNullable(q.selectFrom(participant)
				.where(participant.study.id.eq(studyId))
				.where(participant.user.id.eq(userId))
				.fetchOne());
	}

	public List<Participant> findByUserId(final Long id) {
		return q.selectFrom(participant)
				.where(participant.user.id.eq(id))
				.where(participant.study.status.ne(StudyStatus.COMPLETED))
				.fetch();
	}

	public List<Participant> findCompletedStudyByUserId(final Long id) {
		return q.selectFrom(participant)
				.where(participant.user.id.eq(id))
				.where(participant.study.status.eq(StudyStatus.COMPLETED))
				.fetch();
	}

}
