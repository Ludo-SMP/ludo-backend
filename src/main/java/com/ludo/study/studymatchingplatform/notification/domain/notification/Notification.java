package com.ludo.study.studymatchingplatform.notification.domain.notification;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10)")
	private NotificationEventType notificationEventType;

	@Column(nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "\"read\"", nullable = false)
	private Boolean read;

}
