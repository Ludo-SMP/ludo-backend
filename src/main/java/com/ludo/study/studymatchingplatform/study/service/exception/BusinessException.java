package com.ludo.study.studymatchingplatform.study.service.exception;

public class BusinessException extends RuntimeException {

	public BusinessException(final String message) {
		super(message);
	}

	public BusinessException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
