package com.ludo.study.studymatchingplatform.notification.repository.notification;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.QStudyNotification.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudyNotificationRepositoryImpl {

	private final JPAQueryFactory q;
	private final StudyNotificationJpaRepository studyNotificationJpaRepository;

	public StudyNotification save(final StudyNotification studyNotification) {
		return studyNotificationJpaRepository.save(studyNotification);
	}

	public List<StudyNotification> saveAll(final List<StudyNotification> studyNotifications) {
		return studyNotificationJpaRepository.saveAll(studyNotifications);
	}

	public List<StudyNotification> findAllByUserId(final Long userId) {
		return q.selectFrom(studyNotification)
				.where(studyNotification.notifier.id.eq(userId))
				.fetch();
	}

}
