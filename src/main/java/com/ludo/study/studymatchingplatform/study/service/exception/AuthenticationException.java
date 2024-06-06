package com.ludo.study.studymatchingplatform.study.service.exception;

import com.ludo.study.studymatchingplatform.common.exception.UnauthorizedUserException;

public class AuthenticationException extends UnauthorizedUserException {
    public AuthenticationException(final String message) {
        super(message);
    }
}
