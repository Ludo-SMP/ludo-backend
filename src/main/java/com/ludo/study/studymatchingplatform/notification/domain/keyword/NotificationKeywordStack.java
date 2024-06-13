package com.ludo.study.studymatchingplatform.notification.domain.keyword;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
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
public class NotificationKeywordStack {

	@EmbeddedId
	private NotificationKeywordStackId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("stackId")
	@JoinColumn(name = "stack_id")
	private Stack stack;

	/**
	 * do not initialize: new NotificationKeywordStack();
	 * should use this static method
	 */
	public static NotificationKeywordStack of(final User user, final Stack stack) {
		final NotificationKeywordStackId id = new NotificationKeywordStackId(user.getId(), stack.getId());
		return new NotificationKeywordStack(id, user, stack);
	}

	public Long getStackId() {
		return stack.getId();
	}

}
