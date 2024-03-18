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

	private final Long id;
	private final String accessToken;

	public static AuthUserPayload from(final Claims claims) {
		return new AuthUserPayload(claims.get("id", Long.class), claims.get("accessToken", String.class));
	}

	@Deprecated
	public static AuthUserPayload from(final String userId, final String accessToken) {
		return new AuthUserPayload(Long.parseLong(userId), accessToken);
	}

	public static AuthUserPayload from(final Long userId, final String accessToken) {
		return new AuthUserPayload(userId, accessToken);
	}

	public static AuthUserPayload from(final User user, final String accessToken) {
		return new AuthUserPayload(user.getId(), accessToken);
	}
}
