package com.ludo.study.studymatchingplatform.study.service.exception;

import com.ludo.study.studymatchingplatform.common.exception.UnauthorizedUserException;

public class SocialAccountNotFoundException extends UnauthorizedUserException {

    public SocialAccountNotFoundException(final String message) {
        super(message);
    }

}
