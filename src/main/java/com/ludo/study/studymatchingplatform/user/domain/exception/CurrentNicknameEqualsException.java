package com.ludo.study.studymatchingplatform.user.domain.exception;

import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;

public class CurrentNicknameEqualsException extends BusinessException {

	public static final String MESSAGE = "현재 닉네임과 동일한 닉네임입니다.";

	public CurrentNicknameEqualsException() {
		super(MESSAGE);
	}

}
