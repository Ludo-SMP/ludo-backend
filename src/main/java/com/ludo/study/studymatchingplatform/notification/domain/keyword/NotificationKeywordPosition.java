package com.ludo.study.studymatchingplatform.notification.domain.keyword;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class NotificationKeywordPosition {

	@EmbeddedId
	private NotificationKeywordPositionId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("positionId")
	@JoinColumn(name = "position_id")
	private Position position;

	/**
	 * do not initialize: new NotificationKeywordPosition();
	 * should use this static method
	 */
	public static NotificationKeywordPosition of(final User user, final Position position) {
		final NotificationKeywordPositionId id = new NotificationKeywordPositionId(user.getId(), position.getId());
		return new NotificationKeywordPosition(id, user, position);
	}

	public Long getPositionId() {
		return position.getId();
	}

}
