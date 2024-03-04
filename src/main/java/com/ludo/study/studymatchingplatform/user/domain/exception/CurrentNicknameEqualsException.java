package com.ludo.study.studymatchingplatform.user.domain.exception;

public class CurrentNicknameEqualsException extends RuntimeException {

	public static final String MESSAGE = "현재 닉네임과 동일한 닉네임입니다.";

	public CurrentNicknameEqualsException() {
		super(MESSAGE);
	}

}
