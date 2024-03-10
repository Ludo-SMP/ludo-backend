package com.ludo.study.studymatchingplatform.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ludo.study.studymatchingplatform.common.advice.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Component
@RequiredArgsConstructor
public final class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<CommonResponse> handleException(Exception e) {
		final CommonResponse resp = CommonResponse.error(e.getMessage());
		return new ResponseEntity<>(resp, HttpStatus.CONFLICT);
	}
}
