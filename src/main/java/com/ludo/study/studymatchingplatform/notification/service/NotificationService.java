package com.ludo.study.studymatchingplatform.notification.service;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType.*;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.notification.controller.NotificationKeywordConfigRequest;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationKeywordDto;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final NotificationQueryService notificationQueryService;
	private final NotificationCommandService notificationCommandService;

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

		final Applicant applicant = study.getApplicant(applicantUser);
		applicant.ensureApplicantStatus(ApplicantStatus.ACCEPTED);

		final List<StudyNotification> studyNotifications = notificationCommandService.saveStudyNotifications(study,
				STUDY_APPLICANT_ACCEPT, List.of(applicantUser));

		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public void studyApplicantRejectNotice(final Study study, final User applicantUser) {

		final Applicant applicant = study.getApplicant(applicantUser);
		applicant.ensureApplicantStatus(ApplicantStatus.REFUSED);

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
	public void studyReviewStartNotice(final Study study) {

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

		final List<ReviewNotification> reviewNotifications = notificationCommandService.saveReviewNotifications(review,
				REVIEW_RECEIVE, List.of(reviewee));

		reviewNotifications.forEach(reviewNotification -> {
			final User notifier = reviewNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(reviewNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
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

	public void configNotificationKeywords(final User user,
										   final NotificationKeywordConfigRequest notificationKeywordConfigRequest
	) {
		final NotificationKeywordDto notificationKeywordDto = notificationQueryService
				.findNotificationKeywords(user, notificationKeywordConfigRequest);

		notificationCommandService.updateNotificationKeywords(notificationKeywordDto);
	}

}
