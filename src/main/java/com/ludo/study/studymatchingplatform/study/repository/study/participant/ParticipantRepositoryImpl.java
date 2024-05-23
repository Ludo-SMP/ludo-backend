package com.ludo.study.studymatchingplatform.study.repository.study.participant;

import static com.ludo.study.studymatchingplatform.study.domain.study.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.study.participant.QParticipant.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyEndDateNotifyCondition;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
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

	public List<Participant> findOwnersOfStudiesEndingIn(final StudyEndDateNotifyCondition condition) {
		final LocalDate endDate = LocalDate.now().plusDays(condition.remainingPeriod());
		final LocalDateTime startOfDay = endDate.atStartOfDay();
		final LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

		// TODO: 모집공고 알림 설정을 on한 사용자 쿼리 조건 추가
		return q.select(participant)
				.from(participant)
				.join(participant.study, study)
				.where(participant.role.eq(Role.OWNER))
				.where(study.endDateTime.between(startOfDay, endOfDay))
				.fetch();
	}

}
