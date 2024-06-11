package com.ludo.study.studymatchingplatform.notification.service;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType.*;

import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.service.dto.request.NotificationConfigRequest;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.config.NotificationConfigResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.service.recruitment.position.PositionQueryService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.stack.StackQueryService;
import com.ludo.study.studymatchingplatform.study.service.study.category.CategoryQueryService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final NotificationQueryService notificationQueryService;
	private final NotificationCommandService notificationCommandService;
	private final CategoryQueryService categoryQueryService;
	private final PositionQueryService positionQueryService;
	private final StackQueryService stackQueryService;

	// for notification server sent events
	private final SseEmitters sseEmitters;

	public void recruitmentNotice(final Recruitment recruitment) {

		final List<User> recruitmentNotifiers = notificationQueryService.findRecruitmentNotifier(recruitment);

		final List<RecruitmentNotification> recruitmentNotifications = notificationCommandService.saveRecruitmentNotifications(
				recruitment, RECRUITMENT, recruitmentNotifiers);

		recruitmentNotifications.forEach(recruitmentNotification -> {
			final User notifier = recruitmentNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(recruitmentNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public void studyApplicantNotice(final Recruitment recruitment) {

		final Study study = recruitment.getStudy();
		final List<User> studyApplicantNotifiers = notificationQueryService.findStudyApplicantNotifier(study);

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotifications(study,
				STUDY_APPLICANT, studyApplicantNotifiers);

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public void studyApplicantAcceptNotice(final Study study, final User applicantUser) {

		if (checkNotificationConfigFalse(applicantUser, NotificationConfigGroup.STUDY_APPLICANT_RESULT_CONFIG))
			return;

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotifications(study,
				STUDY_APPLICANT_ACCEPT, List.of(applicantUser));

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public void studyApplicantRejectNotice(final Study study, final User applicantUser) {

		if (checkNotificationConfigFalse(applicantUser, NotificationConfigGroup.STUDY_APPLICANT_RESULT_CONFIG)) {
			return;
		}

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotifications(study,
				STUDY_APPLICANT_REJECT, List.of(applicantUser));

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	// TODO: 스터디 탈퇴 기능 구현 완료되면 해당 서비스에서 호출하는 로직
	public void studyParticipantLeaveNotice(final Study study) {

		final List<User> studyParticipantUsers = notificationQueryService.findStudyParticipantLeaveNotifier(study);

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotifications(study,
				STUDY_PARTICIPANT_LEAVE, studyParticipantUsers);

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	// TODO: 스터디 탈퇴 기능 구현 완료되면 해당 서비스에서 호출하는 로직
	public void studyParticipantLeaveApplyNotice(final Study study) {

		final User studyOwner = study.getOwner();

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotifications(study,
				STUDY_PARTICIPANT_LEAVE_APPLY, List.of(studyOwner));

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	@Scheduled(cron = "0 0 8 * *")
	public void studyEndDateNotice() {

		final List<Participant> ownersOfStudiesEndingIn = notificationQueryService.findStudyEndDateNotifier();

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotificationsWithParticipants(
				STUDY_END_DATE, ownersOfStudiesEndingIn);

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	@Scheduled(cron = "0 0 0 * *")
	public void studyReviewStartNotice() {

		final List<Participant> studyParticipantUsers = notificationQueryService.findStudyReviewStartNotifier();

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotificationsWithParticipants(
				STUDY_REVIEW_START, studyParticipantUsers);

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public void reviewReceiveNotice(final Review review) {

		final User reviewee = review.getReviewee();
		if (checkNotificationConfigFalse(reviewee, NotificationConfigGroup.REVIEW_CONFIG)) {
			return;
		}

		final List<ReviewNotification> reviewNotifications = notificationCommandService.saveReviewNotifications(review,
				REVIEW_RECEIVE, List.of(reviewee));

		reviewNotifications.forEach(reviewNotification -> {
			final User notifier = reviewNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(reviewNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	private boolean checkNotificationConfigFalse(final User notifier, final NotificationConfigGroup reviewConfig) {
		boolean notificationConfig = notificationQueryService.isNotificationConfigTrue(notifier, reviewConfig);
		return Boolean.FALSE.equals(notificationConfig);
	}

	public void reviewPeerFinishNotice(final Study study, final Review review) {

		final List<User> reviewPeerFinishNotifiers = notificationQueryService.findReviewPeerFinishNotifier(study,
				review);

		final List<ReviewNotification> reviewNotifications = notificationCommandService.saveReviewNotifications(review,
				REVIEW_PEER_FINISH, reviewPeerFinishNotifiers);

		reviewNotifications.forEach(reviewNotification -> {
			final User notifier = reviewNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(reviewNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public List<NotificationResponse> findNotifications(final User user) {
		return notificationQueryService.findNotifications(user);
	}

	@Transactional
	public void configGlobalNotificationUserConfig(final User user,
												   final NotificationConfigRequest notificationConfigRequest
	) {
		final GlobalNotificationUserConfig userConfig = notificationQueryService.readNotificationConfig(user);
		notificationCommandService.updateNotificationConfig(user, userConfig, notificationConfigRequest);
	}

	@Transactional
	public void configNotificationCategoryKeywords(final User user,
												   final List<Long> categoryIds
	) {
		// 이미 존재하는 카테고리 찾기
		final Set<Long> existCategoryIds = notificationQueryService.readExistCategoryKeywordCategoryIds(user);

		// 존재하지 않는 카테고리만 필터링
		final List<Long> notExistCategoryIds = filterNotExistIds(categoryIds, existCategoryIds);

		// 필터링된 카테고리에 대해 키워드 생성
		for (Long newCategoryId : notExistCategoryIds) {
			final Category category = categoryQueryService.readCategory(newCategoryId);
			notificationCommandService.saveNotificationKeywordCategory(NotificationKeywordCategory.of(user, category));
		}
	}

	@Transactional
	public void configNotificationPositionKeywords(final User user,
												   final List<Long> positionIds
	) {
		// 이미 존재하는 포지션 찾기
		final Set<Long> existPositionIds = notificationQueryService.readExistPositionKeywordPositionIds(user);

		// 존재하지 않는 포지션만 필터링
		final List<Long> notExistPositionIds = filterNotExistIds(positionIds, existPositionIds);

		for (Long notExistPositionId : notExistPositionIds) {
			final Position position = positionQueryService.readPosition(notExistPositionId);
			notificationCommandService.saveNotificationKeywordPosition(NotificationKeywordPosition.of(user, position));
		}
	}

	@Transactional
	public void configNotificationStackKeywords(final User user,
												final List<Long> positionIds
	) {
		// 이미 존재하는 스택 찾기
		final Set<Long> existStackIds = notificationQueryService.readExistStackKeywordStackIds(user);

		// 존재하지 않는 스택만 필터링
		final List<Long> notExistStackIds = filterNotExistIds(positionIds, existStackIds);

		for (Long notExistStackId : notExistStackIds) {
			final Stack stack = stackQueryService.readStack(notExistStackId);
			notificationCommandService.saveNotificationKeywordStack(NotificationKeywordStack.of(user, stack));
		}
	}

	private List<Long> filterNotExistIds(final List<Long> target, final Set<Long> compare) {
		return target
				.stream()
				.filter(categoryId -> !compare.contains(categoryId))
				.toList();
	}

	public NotificationConfigResponse findNotificationConfig(final User user) {
		return notificationQueryService.readNotificationConfigAndKeywords(user);
	}

	public void checkNotificationsAsRead(final User user, final List<Long> notificationIds) {
		notificationCommandService.updateNotificationsAsRead(user, notificationIds);
	}

}
