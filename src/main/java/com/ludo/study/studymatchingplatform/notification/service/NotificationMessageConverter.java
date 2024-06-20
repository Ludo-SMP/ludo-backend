package com.ludo.study.studymatchingplatform.notification.service;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.notification.domain.notification.Notification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyEndDateNotifyCond;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.message.NotificationMessage;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

@Component
public class NotificationMessageConverter {

	public NotificationMessage convertNotifyMessage(final Notification notification) {
		final NotificationEventType notificationEventType = notification.getNotificationEventType();

		return switch (notificationEventType) {
			case RECRUITMENT -> convertRecruitmentNotifyMessage((RecruitmentNotification)notification);

			case STUDY_APPLICANT -> convertStudyApplicantNotifyMessage((StudyNotification)notification);
			case STUDY_APPLICANT_ACCEPT -> convertStudyApplicantAcceptNotifyMessage((StudyNotification)notification);
			case STUDY_APPLICANT_REJECT -> convertStudyApplicantRejectNotifyMessage((StudyNotification)notification);
			case STUDY_END_DATE -> convertStudyEndDateNotifyMessage((StudyNotification)notification);
			case STUDY_PARTICIPANT_LEAVE ->
					convertStudyParticipantLeaveNotifyMessage((StudyNotification)notification, null);    // TODO
			case STUDY_PARTICIPANT_LEAVE_APPLY ->
					convertStudyParticipantLeaveApplyNotifyMessage((StudyNotification)notification, null);    // TODO
			case STUDY_REVIEW_START -> convertStudyReviewStartNotifyMessage((StudyNotification)notification);

			case REVIEW_RECEIVE -> convertReviewReceiveNotifyMessage((ReviewNotification)notification);
			case REVIEW_PEER_FINISH -> convertReviewPeerFinishNotifyMessage((ReviewNotification)notification);
		};
	}

	private NotificationMessage convertRecruitmentNotifyMessage(final RecruitmentNotification recruitmentNotification) {
		final NotificationEventType notificationEventType = recruitmentNotification.getNotificationEventType();

		final String titleFormat = notificationEventType.getTitleFormat();
		final String contentFormat = notificationEventType.getContentFormat();

		return new NotificationMessage(titleFormat, contentFormat);
	}

	private NotificationMessage convertStudyApplicantNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();
		return new NotificationMessage(title, contentFormat);
	}

	private NotificationMessage convertStudyApplicantAcceptNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(actor.getTitle());
		return new NotificationMessage(title, content);
	}

	private NotificationMessage convertStudyApplicantRejectNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(actor.getTitle());
		return new NotificationMessage(title, content);
	}

	private NotificationMessage convertStudyEndDateNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final Long period = StudyEndDateNotifyCond.remainingPeriod.getPeriod();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle(), period);

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(period);

		return new NotificationMessage(title, content);
	}

	private NotificationMessage convertStudyParticipantLeaveNotifyMessage(final StudyNotification studyNotification,
																		  final User studyLeaver
	) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle(), studyLeaver.getNickname());

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(actor.getTitle(), studyLeaver.getNickname());

		return new NotificationMessage(title, content);
	}

	private NotificationMessage convertStudyParticipantLeaveApplyNotifyMessage(
			final StudyNotification studyNotification,
			final User studyLeaveApplier
	) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle(), studyLeaveApplier.getNickname());

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(studyLeaveApplier.getNickname());

		return new NotificationMessage(title, content);
	}

	private NotificationMessage convertStudyReviewStartNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();

		return new NotificationMessage(title, contentFormat);
	}

	private NotificationMessage convertReviewReceiveNotifyMessage(final ReviewNotification reviewNotification) {
		return convertReviewNotifyMessage(reviewNotification);
	}

	private NotificationMessage convertReviewPeerFinishNotifyMessage(final ReviewNotification reviewNotification) {
		return convertReviewNotifyMessage(reviewNotification);
	}

	private NotificationMessage convertReviewNotifyMessage(final ReviewNotification reviewNotification) {
		final NotificationEventType notificationEventType = reviewNotification.getNotificationEventType();

		final Review actor = reviewNotification.getActor();
		final Study study = actor.getStudy();
		final User reviewer = actor.getReviewer();

		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(study.getTitle(), reviewer.getNickname());

		final String contentFormat = notificationEventType.getContentFormat();

		return new NotificationMessage(title, contentFormat);
	}

}
