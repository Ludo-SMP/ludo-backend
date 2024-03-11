package com.ludo.study.studymatchingplatform.auth.service.google.dto.response;

public record LoginResponse(
		String id,
		String nickname,
		String email
) {
}