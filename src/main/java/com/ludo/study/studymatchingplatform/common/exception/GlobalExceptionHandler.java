package com.ludo.study.studymatchingplatform.common.exception;

import com.ludo.study.studymatchingplatform.common.advice.CommonResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;
import com.ludo.study.studymatchingplatform.study.service.exception.DuplicatedSignUpException;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@Component
@RestControllerAdvice
@RequiredArgsConstructor
public final class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception e) {
        return toResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<CommonResponse> handleException(AuthenticationException e) {
        return toResponseEntity(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<CommonResponse> handleException(BusinessException e) {
        return toResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<CommonResponse> handleException(IllegalArgumentException e) {
        return toResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<CommonResponse> handleException(IllegalStateException e) {
        return toResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<CommonResponse> handleException(DataNotFoundException e) {
        return toResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DataForbiddenException.class)
    public ResponseEntity<CommonResponse> handleException(DataForbiddenException e) {
        return toResponseEntity(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = UnauthorizedUserException.class)
    public ResponseEntity<CommonResponse> handleException(UnauthorizedUserException e) {
        return toResponseEntity(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = DataConflictException.class)
    public ResponseEntity<CommonResponse> handleException(DataConflictException e) {
        return toResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DuplicatedSignUpException.class)
    public ResponseEntity<CommonResponse> duplicatedSignUp(DuplicatedSignUpException e,
                                                           HttpServletResponse response) throws IOException {
        response.sendRedirect("https://ludoapi.store/signup/fail");
        return toResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = SocialAccountNotFoundException.class)
    public ResponseEntity<CommonResponse> noSignUpInformation(SocialAccountNotFoundException e,
                                                              HttpServletResponse response) throws IOException {
        response.sendRedirect("https://ludoapi.store/login/fail");
        return toResponseEntity(e, HttpStatus.NOT_FOUND);
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
