package com.ludo.study.studymatchingplatform.user.domain.exception;

public class DuplicateNicknameException extends RuntimeException {

	public static final String MESSAGE = "이미 존재하는 닉네임입니다.";

	public DuplicateNicknameException() {
		super(MESSAGE);
	}
}
