package com.ludo.study.studymatchingplatform.notification.service;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
			final NotificationResponse notificationResponse = NotificationResponse.from(recruitmentNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(studyNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(studyNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(studyNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(studyNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(studyNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(studyNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(studyNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(reviewNotification);
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
			final NotificationResponse notificationResponse = NotificationResponse.from(reviewNotification);
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
		if (validateKeywordConfigUpdatable(user)) {
			return;
		}

		final Set<NotificationKeywordCategory> requestedKeywordCategory =
				createRequestedKeywordCategories(user, categoryIds);

		final Set<NotificationKeywordCategory> savedKeywordCategories =
				notificationQueryService.readNotificationKeywordCategories(user);

		final Set<NotificationKeywordCategory> keywordCategoriesToSave =
				filterKeywordsToSave(requestedKeywordCategory, savedKeywordCategories);

		final Set<NotificationKeywordCategory> keywordCategoriesToDelete =
				filterKeywordsToDelete(savedKeywordCategories, requestedKeywordCategory);

		saveAndDeleteKeywordCategories(keywordCategoriesToSave, keywordCategoriesToDelete);
	}

	@Transactional
	public void configNotificationPositionKeywords(final User user,
												   final List<Long> positionIds
	) {
		if (validateKeywordConfigUpdatable(user)) {
			return;
		}

		final Set<NotificationKeywordPosition> requestedKeywordPositions =
				createRequestedKeywordPositions(user, positionIds);

		final Set<NotificationKeywordPosition> savedKeywordPositions =
				notificationQueryService.readNotificationKeywordPositions(user);

		final Set<NotificationKeywordPosition> keywordPositionsToSave =
				filterKeywordsToSave(requestedKeywordPositions, savedKeywordPositions);

		final Set<NotificationKeywordPosition> keywordPositionsToDelete =
				filterKeywordsToDelete(savedKeywordPositions, requestedKeywordPositions);

		saveAndDeleteKeywordPositions(keywordPositionsToSave, keywordPositionsToDelete);
	}

	@Transactional
	public void configNotificationStackKeywords(final User user,
												final List<Long> stackIds
	) {
		if (validateKeywordConfigUpdatable(user)) {
			return;
		}

		final Set<NotificationKeywordStack> requestedKeywordStacks =
				createRequestedKeywordStacks(user, stackIds);

		final Set<NotificationKeywordStack> savedKeywordStacks =
				notificationQueryService.readNotificationKeywordStacks(user);

		final Set<NotificationKeywordStack> keywordStacksToSave =
				filterKeywordsToSave(requestedKeywordStacks, savedKeywordStacks);

		final Set<NotificationKeywordStack> keywordStacksToDelete =
				filterKeywordsToDelete(savedKeywordStacks, requestedKeywordStacks);

		saveAndDeleteKeywordStacks(keywordStacksToSave, keywordStacksToDelete);
	}

	private boolean validateKeywordConfigUpdatable(final User user) {
		return !notificationQueryService.isNotificationConfigTrue(user, NotificationConfigGroup.RECRUITMENT_CONFIG);
	}

	private Set<NotificationKeywordCategory> createRequestedKeywordCategories(final User user,
																			  final List<Long> categoryIds
	) {
		return categoryIds.stream()
				.map(categoryId -> {
					final Category category = categoryQueryService.readCategory(categoryId);
					return NotificationKeywordCategory.of(user, category);
				})
				.collect(Collectors.toSet());
	}

	private Set<NotificationKeywordPosition> createRequestedKeywordPositions(final User user,
																			 final List<Long> positionIds
	) {
		return positionIds.stream()
				.map(positionId -> {
					final Position position = positionQueryService.readPosition(positionId);
					return NotificationKeywordPosition.of(user, position);
				})
				.collect(Collectors.toSet());
	}

	private Set<NotificationKeywordStack> createRequestedKeywordStacks(final User user, final List<Long> stackIds) {
		return stackIds.stream()
				.map(stackId -> {
					final Stack stack = stackQueryService.readStack(stackId);
					return NotificationKeywordStack.of(user, stack);
				})
				.collect(Collectors.toSet());
	}

	private <T> Set<T> filterKeywordsToDelete(final Set<T> savedKeywords, final Set<T> requestedKeywords) {
		return savedKeywords.stream()
				.filter(saved -> !requestedKeywords.contains(saved))
				.collect(Collectors.toSet());
	}

	private <T> Set<T> filterKeywordsToSave(final Set<T> requestedKeywords, final Set<T> savedKeywords) {
		return requestedKeywords.stream()
				.filter(requested -> !savedKeywords.contains(requested))
				.collect(Collectors.toSet());
	}

	private void saveAndDeleteKeywordCategories(final Set<NotificationKeywordCategory> keywordCategoriesToSave,
												final Set<NotificationKeywordCategory> keywordCategoriesToDelete
	) {
		notificationCommandService.saveNotificationKeywordCategories(keywordCategoriesToSave);
		notificationCommandService.deleteNotificationKeywordCategories(keywordCategoriesToDelete);
	}

	private void saveAndDeleteKeywordPositions(final Set<NotificationKeywordPosition> keywordPositionsToSave,
											   final Set<NotificationKeywordPosition> keywordPositionsToDelete
	) {
		notificationCommandService.saveNotificationKeywordPositions(keywordPositionsToSave);
		notificationCommandService.deleteNotificationKeywordPositions(keywordPositionsToDelete);
	}

	private void saveAndDeleteKeywordStacks(final Set<NotificationKeywordStack> keywordStacksToSave,
											final Set<NotificationKeywordStack> keywordStacksToDelete
	) {
		notificationCommandService.saveNotificationKeywordStacks(keywordStacksToSave);
		notificationCommandService.deleteNotificationKeywordStacks(keywordStacksToDelete);
	}

	public NotificationConfigResponse findNotificationConfig(final User user) {
		return notificationQueryService.readNotificationConfigAndKeywords(user);
	}

	public void checkNotificationsAsRead(final User user, final List<Long> notificationIds) {
		notificationCommandService.updateNotificationsAsRead(user, notificationIds);
	}

}
