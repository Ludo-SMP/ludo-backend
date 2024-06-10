package com.ludo.study.studymatchingplatform.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.RecruitmentNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.ReviewNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.StudyNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.service.dto.request.NotificationConfigRequest;
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

	private final NotificationKeywordCategoryRepositoryImpl notificationKeywordCategoryRepository;
	private final NotificationKeywordStackRepositoryImpl notificationKeywordStackRepository;
	private final NotificationKeywordPositionRepositoryImpl notificationKeywordPositionRepository;

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

	@Transactional
	public void saveNotificationKeywordCategory(final NotificationKeywordCategory notificationKeywordCategory) {
		notificationKeywordCategoryRepository.save(notificationKeywordCategory);
	}

	@Transactional
	public void saveNotificationKeywordPosition(final NotificationKeywordPosition notificationKeywordPosition) {
		notificationKeywordPositionRepository.save(notificationKeywordPosition);
	}

	@Transactional
	public void saveNotificationKeywordStack(final NotificationKeywordStack notificationKeywordStack) {
		notificationKeywordStackRepository.save(notificationKeywordStack);
	}

	@Transactional
	public void updateNotificationConfig(final User user,
										 final GlobalNotificationUserConfig userConfig,
										 final NotificationConfigRequest notificationConfigRequest
	) {

		final NotificationConfigGroup notificationConfigGroup = notificationConfigRequest.notificationConfigGroup();
		final boolean enabled = notificationConfigRequest.on();
		userConfig.updateConfig(notificationConfigGroup, enabled);

		if (isNotificationKeywordDeleteCondition(notificationConfigGroup, enabled)) {
			notificationKeywordPositionRepository.deleteByUserId(user.getId());
			notificationKeywordCategoryRepository.deleteByUserId(user.getId());
			notificationKeywordStackRepository.deleteByUserId(user.getId());
		}
	}

	private boolean isNotificationKeywordDeleteCondition(final NotificationConfigGroup configGroup,
														 final boolean enabled
	) {
		return isAllConfigOff(configGroup, enabled)
				|| isNotificationConfigOff(configGroup, enabled);
	}

	private boolean isAllConfigOff(final NotificationConfigGroup configGroup, final boolean enabled) {
		return configGroup == NotificationConfigGroup.ALL_CONFIG && !enabled;
	}

	private boolean isNotificationConfigOff(final NotificationConfigGroup configGroup, final boolean enabled) {
		return configGroup == NotificationConfigGroup.RECRUITMENT_CONFIG && !enabled;
	}
}
