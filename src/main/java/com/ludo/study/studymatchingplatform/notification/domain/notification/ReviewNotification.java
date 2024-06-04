package com.ludo.study.studymatchingplatform.notification.domain.notification;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewNotification extends Notification {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	private Review actor;

	private ReviewNotification(final NotificationEventType notificationEventType,
							   final LocalDateTime createdOn,
							   final Review actor,
							   final User notifier
	) {
		super(notificationEventType, createdOn, false, notifier);
		this.actor = actor;
	}

	public static ReviewNotification of(final NotificationEventType notificationEventType,
										final LocalDateTime createdOn,
										final Review actor,
										final User notifier
	) {
		return new ReviewNotification(notificationEventType, createdOn, actor, notifier);
	}
}
