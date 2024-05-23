package com.ludo.study.studymatchingplatform.notification.domain.notification;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewNotification extends Notification {

	// TODO: 신뢰도 기능 브랜치 병합 후, Review 엔티티로 수정 필요
	private Long actor;

}
