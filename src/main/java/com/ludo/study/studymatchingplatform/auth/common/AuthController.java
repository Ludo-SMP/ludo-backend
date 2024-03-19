package com.ludo.study.studymatchingplatform.auth.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final CookieProvider cookieProvider;

	@PostMapping("/auth/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(description = "현재 사용자 로그 아웃")
	@ApiResponse(description = "로그 아웃", responseCode = "204")
	public void logout(
			@Parameter(hidden = true) final HttpServletResponse response) {
		cookieProvider.clearAuthCookie(response);
	}
}
