package com.ludo.study.studymatchingplatform.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.notification.RecruitmentNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.ReviewNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.StudyNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {

	private final StudyNotificationRepositoryImpl studyNotificationRepository;
	private final RecruitmentNotificationRepositoryImpl recruitmentNotificationRepository;
	private final ReviewNotificationRepositoryImpl reviewNotificationRepository;

	@Transactional
	public List<RecruitmentNotification> saveRecruitmentNotifications(final Recruitment actor,
																	  final NotificationEventType notificationType,
																	  final List<User> notifiers
	) {
		final List<RecruitmentNotification> recruitmentNotifications = notifiers
				.stream()
				.map(notifier -> RecruitmentNotification.of(notificationType, LocalDateTime.now(), actor, notifier))
				.toList();

		return recruitmentNotificationRepository.saveAll(recruitmentNotifications);
	}

	@Transactional
	public List<StudyNotification> saveStudyNotifications(final Study actor,
														  final NotificationEventType notificationType,
														  final List<User> notifiers
	) {
		final List<StudyNotification> studyNotifications = notifiers
				.stream()
				.map(notifier -> StudyNotification.of(notificationType, LocalDateTime.now(), actor, notifier))
				.toList();

		return studyNotificationRepository.saveAll(studyNotifications);
	}

	@Transactional
	public List<StudyNotification> saveStudyNotificationsWithParticipants(final NotificationEventType notificationType,
																		  final List<Participant> notifiers
	) {
		return notifiers
				.stream()
				.map(notifier -> StudyNotification.of(
						notificationType, LocalDateTime.now(), notifier.getStudy(), notifier.getUser()))
				.toList();
	}

	@Transactional
	public List<ReviewNotification> saveReviewNotifications(final Review actor,
															final NotificationEventType notificationType,
															final List<User> notifiers
	) {
		final List<ReviewNotification> reviewNotifications = notifiers
				.stream()
				.map(notifier -> ReviewNotification.of(notificationType, LocalDateTime.now(), actor, notifier))
				.toList();

		return reviewNotificationRepository.saveAll(reviewNotifications);
	}

}
