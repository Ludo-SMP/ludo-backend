package com.ludo.study.studymatchingplatform.notification.domain.notification;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReviewNotification extends Notification {

	// TODO: 신뢰도 기능 브랜치 병합 후, Review 엔티티로 수정 필요
	private Long actor;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User notifier;

}
