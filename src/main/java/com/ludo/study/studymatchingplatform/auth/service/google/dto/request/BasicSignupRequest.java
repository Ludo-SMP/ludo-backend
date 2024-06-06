package com.ludo.study.studymatchingplatform.auth.service.google.dto.request;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

public record BasicSignupRequest(
        String nickname,
        String email,
        String password
) {
    public User toUser(final String hashedPassword) {
        return User.basic(nickname, email, hashedPassword);
    }

}
