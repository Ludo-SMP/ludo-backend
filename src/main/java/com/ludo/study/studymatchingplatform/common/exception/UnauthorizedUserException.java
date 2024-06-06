package com.ludo.study.studymatchingplatform.common.exception;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(final String message) {
        super(message);
    }
}