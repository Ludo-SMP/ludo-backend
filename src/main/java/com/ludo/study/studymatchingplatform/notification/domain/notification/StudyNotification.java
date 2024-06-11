package com.ludo.study.studymatchingplatform.notification.domain.notification;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
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
public class StudyNotification extends Notification {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id", nullable = false)
	private Study actor;

	private StudyNotification(final NotificationEventType notificationEventType,
							  final LocalDateTime createdAt,
							  final Study actor,
							  final User notifier
	) {
		super(notificationEventType, createdAt, false, notifier);
		this.actor = actor;
	}

	public static StudyNotification of(final NotificationEventType notificationEventType,
									   final LocalDateTime createdOn,
									   final Study actor,
									   final User notifier
	) {
		return new StudyNotification(notificationEventType, createdOn, actor, notifier);
	}

	public Long getActorId() {
		return actor.getId();
	}

}
