package com.ludo.study.studymatchingplatform.notification.domain.notification;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(of = {"id", "notifier", "notificationEventType", "createdAt", "read"})
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(50)")
	private NotificationEventType notificationEventType;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "\"read\"", nullable = false)
	private Boolean read;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User notifier;

	public Notification(final NotificationEventType notificationEventType, final LocalDateTime createdAt,
						final Boolean read, final User notifier) {
		this.notificationEventType = notificationEventType;
		this.createdAt = createdAt;
		this.read = read;
		this.notifier = notifier;
	}

	public boolean isRead() {
		return read;
	}

}
