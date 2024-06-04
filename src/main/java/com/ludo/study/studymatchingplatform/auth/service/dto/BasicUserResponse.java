package com.ludo.study.studymatchingplatform.auth.service.dto;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

public record BasicUserResponse(
        Long id,
        String nickname,
        String email
) {
    public static BasicUserResponse from(final User user) {
        return new BasicUserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail()
        );
    }
}
