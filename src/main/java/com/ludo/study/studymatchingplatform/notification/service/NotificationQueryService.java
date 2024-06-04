package com.ludo.study.studymatchingplatform.notification.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.controller.NotificationKeywordConfigRequest;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCond;
import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyEndDateNotifierCond;
import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyReviewStartNotifierCond;
import com.ludo.study.studymatchingplatform.notification.repository.notification.RecruitmentNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.ReviewNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.StudyNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationKeywordDto;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
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

	private final NotificationKeywordMapper notificationKeywordMapper;

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

	public NotificationKeywordDto findNotificationKeywords(final User user,
														   final NotificationKeywordConfigRequest keywordConfigRequest
	) {

		return new NotificationKeywordDto(
				notificationKeywordMapper.toKeywordCategories(user, keywordConfigRequest.categoryIds()),
				notificationKeywordMapper.toKeywordStacks(user, keywordConfigRequest.stackIds()),
				notificationKeywordMapper.toKeywordPositions(user, keywordConfigRequest.positionIds())
		);
	}

}
