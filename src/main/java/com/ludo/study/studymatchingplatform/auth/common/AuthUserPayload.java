package com.ludo.study.studymatchingplatform.auth.common;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public final class AuthUserPayload {

	private final long id;

	public static AuthUserPayload from(final Claims claims) {
		return new AuthUserPayload(claims.get("id", Long.class));
	}

	@Deprecated
	public static AuthUserPayload from(final String userId) {
		return new AuthUserPayload(Long.parseLong(userId));
	}

	public static AuthUserPayload from(final Long userId) {
		return new AuthUserPayload(userId);
	}

	public static AuthUserPayload from(final User user) {
		return new AuthUserPayload(user.getId());
	}
}
