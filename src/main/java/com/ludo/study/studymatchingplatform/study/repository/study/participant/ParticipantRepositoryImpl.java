package com.ludo.study.studymatchingplatform.study.repository.study.participant;

import static com.ludo.study.studymatchingplatform.notification.domain.config.QGlobalNotificationUserConfig.*;
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

	public List<Participant> saveAll(final List<Participant> participants) {
		return participantJpaRepository.saveAll(participants);
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
		return q.selectFrom(participant)
				.join(globalNotificationUserConfig).on(participant.user.eq(globalNotificationUserConfig.user))
				.join(participant.study, study)
				.where(participant.role.eq(condition.role()),
						study.endDateTime.between(condition.endDateStartOfDay(), condition.endDateEndOfDay()))
				.fetch();
	}

	public List<Participant> findParticipantsBetweenDateRange(final StudyReviewStartNotifierCond condition) {
		// FIXME 스터디 종료 상태 조건절 추가해야 함. 현재는 스터디 GET API를 호출해야 스터디 시간에 맞게 종료상태로 바뀌는 구조여서, 일단 조건절에서 제외함
		return q.selectFrom(participant)
				.join(globalNotificationUserConfig).on(participant.user.eq(globalNotificationUserConfig.user))
				.join(participant.study, study)
				.where(globalNotificationUserConfig.reviewConfig.isTrue(),
						study.endDateTime.between(condition.yesterdayStartOfDay(), condition.yesterdayEndOfDay()))
				.fetch();
	}
}
