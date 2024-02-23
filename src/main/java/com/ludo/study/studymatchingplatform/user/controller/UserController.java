package com.ludo.study.studymatchingplatform.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.Redirection;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.service.UserService;
import com.ludo.study.studymatchingplatform.user.service.dto.Me;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final CookieProvider cookieProvider;

	private final Redirection redirection;

	@DeleteMapping("/users/deactivate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void withdraw(@AuthUser final User user, final HttpServletResponse response) {
		userService.withdraw(user);
		cookieProvider.clearAuthCookie(response);
		redirection.to("/", response);
	}

	@GetMapping("/users/me")
	public ResponseEntity<Me> fetchMe(@AuthUser final User user) {
		return new ResponseEntity<>(Me.from(user), HttpStatus.OK);
	}

}