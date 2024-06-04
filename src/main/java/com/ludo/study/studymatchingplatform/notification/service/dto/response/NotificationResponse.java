package com.ludo.study.studymatchingplatform.notification.service.dto.response;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;

public record NotificationResponse(
		Long notificationId,
		String title,
		String content,
		String type,
		boolean read,
		LocalDateTime createdAt

		//TODO: API Params 반영
) {

	public NotificationResponse(final RecruitmentNotification notification) {
		this(notification.getId(),
				notification.getNotificationEventType().getTitle(),
				notification.getNotificationEventType().getContent(),
				notification.getNotificationEventType().toString(),
				notification.getRead(),
				notification.getCreatedAt());
	}

	public NotificationResponse(final StudyNotification notification) {
		this(notification.getId(),
				notification.getNotificationEventType().getTitle(),
				notification.getNotificationEventType().getContent(),
				notification.getNotificationEventType().toString(),
				notification.getRead(),
				notification.getCreatedAt());
	}

	public NotificationResponse(final ReviewNotification notification) {
		this(notification.getId(),
				notification.getNotificationEventType().getTitle(),
				notification.getNotificationEventType().getContent(),
				notification.getNotificationEventType().toString(),
				notification.getRead(),
				notification.getCreatedAt());
	}

}
