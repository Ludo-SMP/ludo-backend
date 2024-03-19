package com.ludo.study.studymatchingplatform.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ludo.study.studymatchingplatform.common.advice.CommonResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RestControllerAdvice
@RequiredArgsConstructor
public final class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<CommonResponse> handleException(Exception e) {
		return toResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = {AuthenticationException.class})
	public ResponseEntity<CommonResponse> handleException(final AuthenticationException e) {
		return toResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = {BusinessException.class, IllegalArgumentException.class, IllegalStateException.class})
	public ResponseEntity<CommonResponse> handleException(final IllegalArgumentException e) {
		return toResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity toResponseEntity(final Exception e, final HttpStatus status) {
		final CommonResponse resp = CommonResponse.error(e.getMessage());
		log(e);
		return new ResponseEntity<>(resp, status);
	}

	private void log(final Exception e) {
		log.info("[] [Exception]: Kind: {}, Message: {}", e.getClass().getSimpleName(), e.getMessage());
	}

}
