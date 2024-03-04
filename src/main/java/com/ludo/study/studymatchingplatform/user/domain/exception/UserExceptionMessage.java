package com.ludo.study.studymatchingplatform.user.domain.exception;

import lombok.Getter;

@Getter
public enum UserExceptionMessage {

	INIT_DEFAULT_NICKNAME("초기 닉네임은 id가 존재해야 합니다.");

	private final String message;

	UserExceptionMessage(String message) {
		this.message = message;
	}

}
