package com.ludo.study.studymatchingplatform.auth.service.dto;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

public record BasicLoginResponse(
        BasicUserResponse user,
        String accessToken
) {
    public static BasicLoginResponse of(final User user, final String accessToken) {
        return new BasicLoginResponse(
                BasicUserResponse.from(user),
                accessToken
        );
    }
}
