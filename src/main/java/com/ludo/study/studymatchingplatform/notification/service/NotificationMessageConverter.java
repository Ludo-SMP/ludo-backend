package com.ludo.study.studymatchingplatform.notification.service;

import org.springframework.stereotype.Component;

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

	public NotificationMessage convertRecruitmentNotifyMessage(final RecruitmentNotification recruitmentNotification) {
		final NotificationEventType notificationEventType = recruitmentNotification.getNotificationEventType();

		final String titleFormat = notificationEventType.getTitleFormat();
		final String contentFormat = notificationEventType.getContentFormat();

		return new NotificationMessage(titleFormat, contentFormat);
	}

	public NotificationMessage convertStudyApplicantNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();
		return new NotificationMessage(title, contentFormat);
	}

	public NotificationMessage convertStudyApplicantAcceptNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(actor.getTitle());
		return new NotificationMessage(title, content);
	}

	public NotificationMessage convertStudyApplicantRejectNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(actor.getTitle());
		return new NotificationMessage(title, content);
	}

	public NotificationMessage convertStudyEndDateNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final Long period = StudyEndDateNotifyCond.remainingPeriod.getPeriod();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle(), period);

		final String contentFormat = notificationEventType.getContentFormat();
		final String content = contentFormat.formatted(period);

		return new NotificationMessage(title, content);
	}

	public NotificationMessage convertStudyParticipantLeaveNotifyMessage(final StudyNotification studyNotification,
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

	public NotificationMessage convertStudyParticipantLeaveApplyNotifyMessage(final StudyNotification studyNotification,
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

	public NotificationMessage convertStudyReviewStartNotifyMessage(final StudyNotification studyNotification) {
		final NotificationEventType notificationEventType = studyNotification.getNotificationEventType();

		final Study actor = studyNotification.getActor();
		final String titleFormat = notificationEventType.getTitleFormat();
		final String title = titleFormat.formatted(actor.getTitle());

		final String contentFormat = notificationEventType.getContentFormat();

		return new NotificationMessage(title, contentFormat);
	}

	public NotificationMessage convertReviewReceiveNotifyMessage(final ReviewNotification reviewNotification) {
		return convertReviewNotifyMessage(reviewNotification);
	}

	public NotificationMessage convertReviewPeerFinishNotifyMessage(final ReviewNotification reviewNotification) {
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
