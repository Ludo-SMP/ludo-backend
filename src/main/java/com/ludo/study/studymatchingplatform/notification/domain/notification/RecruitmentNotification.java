package com.ludo.study.studymatchingplatform.notification.domain.notification;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(of = {"actor"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentNotification extends Notification {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id", nullable = false)
	private Recruitment actor;

	private RecruitmentNotification(final NotificationEventType notificationEventType,
									final LocalDateTime createdOn,
									final Recruitment actor,
									final User notifier
	) {
		super(notificationEventType, createdOn, false, notifier);
		this.actor = actor;
	}

	public static RecruitmentNotification of(final NotificationEventType notificationEventType,
											 final LocalDateTime createdOn,
											 final Recruitment actor,
											 final User notifier
	) {
		return new RecruitmentNotification(notificationEventType, createdOn, actor, notifier);
	}

	public Long getActorId() {
		return actor.getId();
	}

}
