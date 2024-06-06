package com.ludo.study.studymatchingplatform.notification.domain.keyword;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationKeywordStack {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_keyword_stack_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stack_id")
	private Stack stack;

	private NotificationKeywordStack(final User user, final Stack stack) {
		this.user = user;
		this.stack = stack;
	}

	public static NotificationKeywordStack of(final User user, final Stack stack) {
		return new NotificationKeywordStack(user, stack);
	}

	public Long getStackId() {
		return stack.getId();
	}
}
