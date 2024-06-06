package com.ludo.study.studymatchingplatform.study.repository.study.participant;

import static com.ludo.study.studymatchingplatform.study.domain.study.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.study.participant.QParticipant.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyEndDateNotifierCond;
import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyReviewStartNotifierCond;
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
				.where(participant.deletedDateTime.isNull())
				.where(participant.study.status.ne(StudyStatus.COMPLETED))
				.fetch();
	}

	public List<Participant> findCompletedStudyByUserId(final Long id) {
		return q.selectFrom(participant)
				.where(participant.user.id.eq(id))
				.where(participant.deletedDateTime.isNull())
				.where(participant.study.status.eq(StudyStatus.COMPLETED))
				.fetch();
	}

	public List<Participant> findOwnerParticipantsBetweenDateRange(final StudyEndDateNotifierCond condition) {
		// TODO: 모집공고 알림 설정을 on한 사용자 쿼리 조건 추가
		return q.selectFrom(participant)
				.join(participant.study, study)
				.where(participant.role.eq(condition.role()))
				.where(study.endDateTime.between(condition.startOfDay(), condition.endOfDay()))
				.fetch();
	}

	public List<Participant> findParticipantsBetweenDateRangeAndCompleted(
			final StudyReviewStartNotifierCond condition) {
		// TODO: 모집공고 알림 설정을 on한 사용자 쿼리 조건 추가
		return q.selectFrom(participant)
				.join(participant.study, study)
				.where(participant.study.status.eq(condition.studyStatus()))
				.where(study.endDateTime.between(condition.startOfDay(), condition.endOfDay()))
				.fetch();
	}
}
