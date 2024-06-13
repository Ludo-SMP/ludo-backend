package com.ludo.study.studymatchingplatform.notification.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyEndDateNotifyCond;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.config.GlobalNotificationUserConfigRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCond;
import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyEndDateNotifierCond;
import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyReviewStartNotifierCond;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.RecruitmentNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.ReviewNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.StudyNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.config.NotificationConfigResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationQueryService {

	private final UserRepositoryImpl userRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final ReviewRepositoryImpl reviewRepository;

	private final StudyNotificationRepositoryImpl studyNotificationRepository;
	private final RecruitmentNotificationRepositoryImpl recruitmentNotificationRepository;
	private final ReviewNotificationRepositoryImpl reviewNotificationRepository;

	private final GlobalNotificationUserConfigRepositoryImpl globalNotificationUserConfigRepository;
	private final NotificationKeywordCategoryRepositoryImpl notificationKeywordCategoryRepository;
	private final NotificationKeywordPositionRepositoryImpl notificationKeywordPositionRepository;
	private final NotificationKeywordStackRepositoryImpl notificationKeywordStackRepository;

	private final UtcDateTimePicker utcDateTimePicker;

	public List<User> findRecruitmentNotifier(final Recruitment recruitment) {
		final RecruitmentNotifierCond recruitmentNotifierCondition = new RecruitmentNotifierCond(
				recruitment.getOwner(),
				recruitment.getCategory(),
				recruitment.getPositions(),
				recruitment.getStacks());
		return userRepository.findRecruitmentNotifiers(recruitmentNotifierCondition);
	}

	public List<User> findStudyApplicantNotifier(final Study study) {
		return userRepository.findStudyApplicantNotifiers(study.getId());
	}

	public boolean isNotificationConfigTrue(final User applicantUser, final NotificationConfigGroup configGroup) {
		return globalNotificationUserConfigRepository.isUserNotificationConfigIsTrue(
				applicantUser.getId(), configGroup);
	}

	public List<User> findStudyParticipantLeaveNotifier(final Study study) {
		return userRepository.findParticipantLeaveNotifiers(study.getId());
	}

	public List<Participant> findStudyEndDateNotifier() {
		final LocalDate studyEndDate = StudyEndDateNotifyCond.remainingPeriod.getStudyEndDate(utcDateTimePicker);
		final LocalDateTime studyEndDateStartOfDay = StudyEndDateNotifyCond.remainingPeriod.getStudyEndDateStartOfDay(
				utcDateTimePicker, studyEndDate);
		final LocalDateTime studyEndDateEndOfDay = StudyEndDateNotifyCond.remainingPeriod.getStudyEndDateEndOfDay(
				utcDateTimePicker, studyEndDate);

		final List<Participant> ownerParticipantsBetweenDateRange = participantRepository.findOwnerParticipantsBetweenDateRange(
				new StudyEndDateNotifierCond(Role.OWNER, studyEndDateStartOfDay, studyEndDateEndOfDay));

		log.info("ownerParticipantsBetweenDateRange = {}", ownerParticipantsBetweenDateRange);
		return ownerParticipantsBetweenDateRange;
	}

	public List<Participant> findStudyReviewStartNotifier() {
		final LocalDate yesterday = utcDateTimePicker.now().minusDays(1).toLocalDate();
		final LocalDateTime yesterdayStartOfDay = utcDateTimePicker.toMicroSeconds(yesterday.atStartOfDay());
		final LocalDateTime yesterdayEndOfDay = utcDateTimePicker.toMicroSeconds(yesterday.atTime(LocalTime.MAX));
		final StudyReviewStartNotifierCond studyReviewStartNotifyCond = new StudyReviewStartNotifierCond(
				yesterdayStartOfDay,
				yesterdayEndOfDay);

		final List<Participant> participantsBetweenDateRange = participantRepository
				.findParticipantsBetweenDateRange(studyReviewStartNotifyCond);
		log.info("participantsBetweenDateRange = {}", participantsBetweenDateRange);
		return participantsBetweenDateRange;
	}

	public List<User> findReviewPeerFinishNotifier(final Study study, final Review review) {
		final User reviewer = review.getReviewer();
		final User reviewee = review.getReviewee();
		if (!isPeerReviewFinish(study, reviewee, reviewer)) {
			return Collections.emptyList();
		}
		final GlobalNotificationUserConfig reviewerNotificationConfig = readGlobalNotificationUserConfig(reviewer);
		final GlobalNotificationUserConfig revieweeNotificationConfig = readGlobalNotificationUserConfig(reviewee);

		return Stream.of(
						Boolean.TRUE.equals(reviewerNotificationConfig.getReviewConfig()) ? reviewer : null,
						Boolean.TRUE.equals(revieweeNotificationConfig.getReviewConfig()) ? reviewee : null)
				.filter(Objects::nonNull)
				.toList();
	}

	private GlobalNotificationUserConfig readGlobalNotificationUserConfig(final User user) {
		return globalNotificationUserConfigRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("알림 설정이 등록되어 있지 않습니다."));
	}

	private boolean isPeerReviewFinish(final Study study, final User reviewee, final User reviewer) {
		return reviewRepository.exists(study.getId(), reviewee.getId(), reviewer.getId());
	}

	@Transactional
	public List<NotificationResponse> findNotifications(final User user) {
		final Long userId = user.getId();
		final List<RecruitmentNotification> recruitmentNotifications = recruitmentNotificationRepository
				.findAllByUserId(userId);
		final List<StudyNotification> studyNotifications = studyNotificationRepository.findAllByUserId(userId);
		final List<ReviewNotification> reviewNotifications = reviewNotificationRepository.findAllByUserId(userId);

		final List<NotificationResponse> notificationResponses = new ArrayList<>();
		recruitmentNotifications.stream()
				.map(NotificationResponse::from)
				.forEach(notificationResponses::add);

		studyNotifications.stream()
				.map(NotificationResponse::from)
				.forEach(notificationResponses::add);

		reviewNotifications.stream()
				.map(NotificationResponse::from)
				.forEach(notificationResponses::add);

		return notificationResponses;
	}

	public NotificationConfigResponse readNotificationConfigAndKeywords(final User user) {
		final GlobalNotificationUserConfig userConfig = findNotificationConfig(user);

		final List<NotificationKeywordCategory> notificationKeywordCategories = notificationKeywordCategoryRepository
				.findByUserId(user.getId());
		final List<NotificationKeywordPosition> notificationKeywordPositions = notificationKeywordPositionRepository
				.findByUserId(user.getId());
		final List<NotificationKeywordStack> notificationKeywordStacks = notificationKeywordStackRepository
				.findByUserId(user.getId());

		return NotificationConfigResponse.fromUserConfig(userConfig,
				notificationKeywordCategories, notificationKeywordPositions, notificationKeywordStacks);
	}

	public GlobalNotificationUserConfig readNotificationConfig(final User user) {
		return findNotificationConfig(user);
	}

	private GlobalNotificationUserConfig findNotificationConfig(User user) {
		return globalNotificationUserConfigRepository
				.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("id={%d} 사용자의 알림 설정이 등록되지 않았습니다.", user.getId())));
	}

	public Set<NotificationKeywordPosition> readNotificationKeywordPositions(final User user) {
		return new HashSet<>(notificationKeywordPositionRepository.findByUserId(user.getId()));
	}

	public Set<NotificationKeywordStack> readNotificationKeywordStacks(final User user) {
		return new HashSet<>(notificationKeywordStackRepository.findByUserId(user.getId()));
	}

	public Set<NotificationKeywordCategory> readNotificationKeywordCategories(final User user) {
		return new HashSet<>(notificationKeywordCategoryRepository.findByUserId(user.getId()));
	}

}
