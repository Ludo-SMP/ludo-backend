package com.ludo.study.studymatchingplatform.user.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.Redirection;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.service.ChangeNicknameService;
import com.ludo.study.studymatchingplatform.user.service.UserService;
import com.ludo.study.studymatchingplatform.user.service.dto.request.ChangeUserNicknameRequest;
import com.ludo.study.studymatchingplatform.user.service.dto.response.ChangeUserNicknameResponse;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;
	private final ChangeNicknameService changeNicknameService;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;
	private final Redirection redirection;

	@DeleteMapping("/users/deactivate")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "회원 탈퇴")
	@ApiResponse(description = "회원 탈퇴 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public void withdraw(@Parameter(hidden = true) @AuthUser final User user, final HttpServletResponse response) throws
			IOException {
		userService.withdraw(user);
		cookieProvider.clearAuthCookie(response);
		redirection.to("/", response);
	}

	@GetMapping("/users/me")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "로그인 된 사용자 정보 조회")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	@DataFieldName("user")
	public UserResponse getMe(@Parameter(hidden = true) @AuthUser final User user,
							  final HttpServletResponse response) {
		final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));
		cookieProvider.setAuthCookie(accessToken, response);
		return UserResponse.from(user);
	}

	@PostMapping("/users/me/nickname")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "사용자 닉네임 변경")
	@ApiResponse(description = "닉네임 변경 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	@DataFieldName("user")
	public ChangeUserNicknameResponse changeNickname(
			@Parameter(hidden = true) @AuthUser final User user,
			@RequestBody @Valid final ChangeUserNicknameRequest changeNickname) {
		return changeNicknameService.changeUserNickname(user,
				changeNickname.changeNickname());
	}

}
