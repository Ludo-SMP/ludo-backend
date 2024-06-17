package com.ludo.study.studymatchingplatform.common.exception;

public class AccessTokenNotFoundException extends UnauthorizedUserException {
	public AccessTokenNotFoundException() {
		super("액세스 토큰이 없습니다.");
	}

	public AccessTokenNotFoundException(final String message) {
		super(message);
	}
}
