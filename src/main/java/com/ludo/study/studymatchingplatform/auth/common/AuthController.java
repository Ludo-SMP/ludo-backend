package com.ludo.study.studymatchingplatform.auth.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final CookieProvider cookieProvider;

	@PostMapping("/auth/logout")
	@ApiResponse(description = "로그 아웃 수행", responseCode = "200")
	public ResponseEntity<BaseApiResponse<Void>> logout(@Parameter(hidden = true) final HttpServletResponse response) {
		cookieProvider.clearAuthCookie(response);
		return ResponseEntity.ok(BaseApiResponse.success("로그아웃 성공", null));
	}
}
