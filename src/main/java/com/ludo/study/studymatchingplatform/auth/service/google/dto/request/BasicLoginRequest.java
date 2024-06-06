package com.ludo.study.studymatchingplatform.auth.service.google.dto.request;

public record BasicLoginRequest(
        String email,
        String password
) {

}
