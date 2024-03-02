package com.ludo.study.studymatchingplatform.auth.common;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final CookieProvider cookieProvider;

	private final Redirection redirection;

	@PostMapping("/auth/logout")
	public void logout(final HttpServletResponse response) throws IOException {
		cookieProvider.clearAuthCookie(response);
		redirection.to("/", response);
	}
}
