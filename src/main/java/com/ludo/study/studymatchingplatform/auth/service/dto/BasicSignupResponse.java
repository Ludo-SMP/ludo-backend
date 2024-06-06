package com.ludo.study.studymatchingplatform.auth.service.dto;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

public record BasicSignupResponse(
        BasicUserResponse user,
        String accessToken
) {

    public static BasicSignupResponse of(final User user, final String accessToken) {
        return new BasicSignupResponse(
                BasicUserResponse.from(user),
                accessToken
        );
    }
}
