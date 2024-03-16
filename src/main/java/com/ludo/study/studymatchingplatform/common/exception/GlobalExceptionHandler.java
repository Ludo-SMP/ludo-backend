package com.ludo.study.studymatchingplatform.common.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ludo.study.studymatchingplatform.common.advice.CommonResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;

import jakarta.servlet.http.HttpServletResponse;
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

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<CommonResponse> duplicatedSignUp(IllegalArgumentException e,
														   HttpServletResponse response) throws IOException {
		final CommonResponse resp = CommonResponse.error(e.getMessage());
		response.sendRedirect("http://localhost:3000");
		return new ResponseEntity<>(resp, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<CommonResponse> noSignUpInformation(NotFoundException e,
															  HttpServletResponse response) throws IOException {
		final CommonResponse resp = CommonResponse.error(e.getMessage());
		response.sendRedirect("http://localhost:3000");
		return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
	}

}
