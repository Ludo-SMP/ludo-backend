package com.ludo.study.studymatchingplatform.auth.service.google.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleOAuthToken {

	@JsonProperty("access_token")
	private final String accessToken;

	@JsonProperty("refresh_token")
	private final String refreshToken;

}
