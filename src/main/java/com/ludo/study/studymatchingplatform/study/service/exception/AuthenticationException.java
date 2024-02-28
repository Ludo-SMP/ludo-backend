package com.ludo.study.studymatchingplatform.study.service.exception;

public class AuthenticationException extends BusinessException {

	public AuthenticationException(final String message) {
		super(message);
	}

	public AuthenticationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
