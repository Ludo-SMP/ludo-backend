package com.ludo.study.studymatchingplatform.notification.service.dto.response;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.notification.domain.notification.Notification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.message.NotificationMessage;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.route.NotificationRouteParameter;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.route.RecruitmentNotificationRoute;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.route.ReviewNotificationRoute;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.route.StudyNotificationRoute;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;

public record NotificationResponse(Long notificationId,
								   String title,
								   String content,
								   String type,
								   boolean read,
								   LocalDateTime createdAt,
								   NotificationRouteParameter params
) {

	private NotificationResponse(final Notification notification,
								 final NotificationMessage notificationMessage,
								 final NotificationRouteParameter params
	) {
		this(
				notification.getId(),
				notificationMessage.title(),
				notificationMessage.content(),
				notification.getNotificationEventType().toString(),
				notification.getRead(),
				notification.getCreatedAt(),
				params
		);
	}

	public static NotificationResponse from(final RecruitmentNotification notification,
											final NotificationMessage notificationMessage
	) {
		final NotificationRouteParameter routeParam = new RecruitmentNotificationRoute(notification.getActorId());

		return new NotificationResponse(notification, notificationMessage, routeParam);
	}

	public static NotificationResponse from(final ReviewNotification notification,
											final NotificationMessage notificationMessage
	) {
		final Review review = notification.getActor();
		final NotificationRouteParameter routeParam = new ReviewNotificationRoute(review.getStudyId(),
				review.getReviewerId());

		return new NotificationResponse(notification, notificationMessage, routeParam);
	}

	public static NotificationResponse from(final StudyNotification notification,
											final NotificationMessage notificationMessage
	) {
		final NotificationRouteParameter routeParam = new StudyNotificationRoute(notification.getActorId());

		return new NotificationResponse(notification, notificationMessage, routeParam);
	}

}
