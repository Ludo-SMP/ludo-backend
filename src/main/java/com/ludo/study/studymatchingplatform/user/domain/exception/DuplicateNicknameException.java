package com.ludo.study.studymatchingplatform.user.domain.exception;

import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;

public class DuplicateNicknameException extends BusinessException {

	public static final String MESSAGE = "이미 존재하는 닉네임입니다.";

	public DuplicateNicknameException() {
		super(MESSAGE);
	}
}
