package com.ludo.study.studymatchingplatform.auth.service.naver.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverUserProfile {

	@JsonProperty("resultcode")
	private String resultCode;

	private String message;

	private Response response;

	@Getter
	@ToString
	public static class Response {
		private String id;
		private String email;
	}

	public String getEmail() {
		return response.getEmail();
	}

	public User toUser() {
		return User.builder()
				.social(Social.NAVER)
				.email(response.email)
				.build();
	}

}
