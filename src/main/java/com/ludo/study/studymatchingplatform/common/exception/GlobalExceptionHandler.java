package com.ludo.study.studymatchingplatform.common.exception;

import java.io.IOException;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ludo.study.studymatchingplatform.common.advice.CommonResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;
import com.ludo.study.studymatchingplatform.study.service.exception.DuplicatedSignUpException;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RestControllerAdvice
@RequiredArgsConstructor
public final class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<CommonResponse> handleException(Exception e) {
		return toResponseEntity(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<CommonResponse> handleException(AuthenticationException e) {
		log.info("AuthenticationException -> UNAUTHORIZED");
		return toResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity<CommonResponse> handleException(BusinessException e) {
		log.info("BusinessException -> BAD REQUEST");
		return toResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = IllegalStateException.class)
	public ResponseEntity<CommonResponse> handleException(IllegalStateException e) {
		log.info("IllegalStateException -> BAD REQUEST");
		return toResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicatedSignUpException.class)
	public ResponseEntity<CommonResponse> duplicatedSignUp(DuplicatedSignUpException e,
														   HttpServletResponse response) throws IOException {
		log.info("DuplicatedSignUpException -> CONFLICT");
		response.sendRedirect("https://ludoapi.store/signup/fail");
		return toResponseEntity(e, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = ChangeSetPersister.NotFoundException.class)
	public ResponseEntity<CommonResponse> noSignUpInformation(ChangeSetPersister.NotFoundException e,
															  HttpServletResponse response) throws IOException {
		response.sendRedirect("https://ludoapi.store/login/fail");
		log.info("NotFoundException -> NOT_FOUND");
		return toResponseEntity(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<CommonResponse> handleException(HttpMessageNotReadableException e) {
		log(e);
		final String invalidJsonRequestMessage = "JSON 형식이 잘못되었습니다.";
		log.info("HttpMessageNotReadableException -> BAD_REQUEST");
		return toResponseEntity(e, invalidJsonRequestMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AccessTokenNotFoundException.class)
	public ResponseEntity<CommonResponse> handleException(AccessTokenNotFoundException e) {
		log.info("MustLoginException -> BAD_REQUEST");
		return toResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<CommonResponse> handleException(IllegalArgumentException e) {
		log.info("IllegalArgumentException -> BAD_REQUEST");
		return toResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<CommonResponse> handleException(DataNotFoundException e) {
		log.info("DataNotFoundException -> NOT_FOUND");
		return toResponseEntity(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = DataForbiddenException.class)
	public ResponseEntity<CommonResponse> handleException(DataForbiddenException e) {
		log.info("DataForbiddenException -> FORBIDDEN");
		return toResponseEntity(e, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(value = UnauthorizedUserException.class)
	public ResponseEntity<CommonResponse> handleException(UnauthorizedUserException e) {
		log.info("UnauthorizedUserException -> UNAUTHORIZED");
		return toResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = DataConflictException.class)
	public ResponseEntity<CommonResponse> handleException(DataConflictException e) {
		log.info("DataConflictException -> DataConflictException");
		return toResponseEntity(e, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = SocialAccountNotFoundException.class)
	public ResponseEntity<CommonResponse> noSignUpInformation(SocialAccountNotFoundException e,
															  HttpServletResponse response) throws IOException {
		response.sendRedirect("https://ludoapi.store/login/fail");
		log.info("SocialAccountNotFoundException -> NOT_FOUND");
		return toResponseEntity(e, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<CommonResponse> toResponseEntity(Exception e, String errorMessage, HttpStatus status) {
		final CommonResponse resp = CommonResponse.error(errorMessage);
		log(e);
		return new ResponseEntity<>(resp, status);
	}

	private ResponseEntity<CommonResponse> toResponseEntity(Exception e, HttpStatus status) {
		final CommonResponse resp = CommonResponse.error(e.getMessage());
		log(e);
		return new ResponseEntity<>(resp, status);
	}

	private void log(final Exception e) {
		log.info("[] [Exception]: Kind: {}, Message: {}", e.getClass().getSimpleName(), e.getMessage());
	}

}
