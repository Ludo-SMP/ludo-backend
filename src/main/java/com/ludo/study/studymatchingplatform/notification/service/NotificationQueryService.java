package com.ludo.study.studymatchingplatform.notification.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
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
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
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

	public List<User> findRecruitmentNotifier(final Recruitment recruitment) {
		final RecruitmentNotifierCond recruitmentNotifierCondition = new RecruitmentNotifierCond(
				recruitment.getOwner(),
				recruitment.getCategory(),
				recruitment.getPositions(),
				recruitment.getStacks());
		return userRepository.findRecruitmentNotifiers(recruitmentNotifierCondition);
	}

	public List<User> findStudyApplicantNotifier(final Study study) {
		return userRepository.findParticipantUsersByStudyId(study.getId());
	}

	public List<User> findStudyParticipantLeaveNotifier(final Study study) {
		return userRepository.findParticipantUsersByStudyId(study.getId());
	}

	public List<Participant> findStudyEndDateNotifier() {
		final Long remainingPeriod = 5L;
		final LocalDate endDate = LocalDate.now()
				.plusDays(remainingPeriod);    // TODO: UtcTimePicker PR merge 후 수정 (for Testable Code)
		final LocalDateTime startOfDay = endDate.atStartOfDay();
		final LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

		return participantRepository.findOwnerParticipantsBetweenDateRange(
				new StudyEndDateNotifierCond(Role.OWNER, startOfDay, endOfDay));
	}

	public List<Participant> findStudyReviewStartNotifier() {
		final LocalDate now = LocalDate.now();
		final LocalDateTime startOfDay = now.atStartOfDay(); // TODO: UtcTimePicker PR merge 후 수정 (for Testable Code)
		final LocalDateTime endOfDay = now.atTime(LocalTime.MAX);
		final StudyReviewStartNotifierCond studyReviewStartNotifyCond = new StudyReviewStartNotifierCond(
				startOfDay,
				endOfDay,
				StudyStatus.COMPLETED);

		return participantRepository.findParticipantsBetweenDateRangeAndCompleted(studyReviewStartNotifyCond);
	}

	public List<User> findReviewPeerFinishNotifier(final Study study, final Review review) {
		final User reviewer = review.getReviewer();
		final User reviewee = review.getReviewee();
		if (!isPeerReviewFinish(study, reviewee, reviewer)) {
			return Collections.emptyList();
		}

		return List.of(reviewer, reviewee);
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
				.map(NotificationResponse::new)
				.forEach(notificationResponses::add);

		studyNotifications.stream()
				.map(NotificationResponse::new)
				.forEach(notificationResponses::add);

		reviewNotifications.stream()
				.map(NotificationResponse::new)
				.forEach(notificationResponses::add);

		return notificationResponses;
	}

	public NotificationConfigResponse readNotificationConfigAndKeywords(final User user) {
		final GlobalNotificationUserConfig userConfig = findNotificationConfig(
				user);

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

	public Set<Long> readExistCategoryKeywordCategoryIds(final User user) {
		final List<NotificationKeywordCategory> notificationKeywordCategories = notificationKeywordCategoryRepository
				.findByUserId(user.getId());

		return notificationKeywordCategories
				.stream()
				.map(NotificationKeywordCategory::getCategoryId)
				.collect(Collectors.toSet());
	}

	public Set<Long> readExistPositionKeywordPositionIds(final User user) {
		final List<NotificationKeywordPosition> notificationKeywordPositions = notificationKeywordPositionRepository
				.findByUserId(user.getId());

		return notificationKeywordPositions
				.stream()
				.map(NotificationKeywordPosition::getPositionId)
				.collect(Collectors.toSet());
	}

	public Set<Long> readExistStackKeywordStackIds(final User user) {
		final List<NotificationKeywordStack> notificationKeywordStacks = notificationKeywordStackRepository
				.findByUserId(user.getId());

		return notificationKeywordStacks
				.stream()
				.map(NotificationKeywordStack::getStackId)
				.collect(Collectors.toSet());
	}

}
