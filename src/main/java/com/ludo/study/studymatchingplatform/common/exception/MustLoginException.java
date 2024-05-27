package com.ludo.study.studymatchingplatform.common.exception;

public class MustLoginException extends UnauthorizedUserException {
    public MustLoginException() {
        super("로그인이 필요한 서비스입니다.");
    }

    public MustLoginException(final String message) {
        super(message);
    }
}
