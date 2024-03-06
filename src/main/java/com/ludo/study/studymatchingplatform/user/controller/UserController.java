package com.ludo.study.studymatchingplatform.user.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.Redirection;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.study.controller.dto.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.service.ChangeNicknameService;
import com.ludo.study.studymatchingplatform.user.service.UserService;
import com.ludo.study.studymatchingplatform.user.service.dto.request.ChangeUserNicknameRequest;
import com.ludo.study.studymatchingplatform.user.service.dto.response.ChangeUserNicknameResponse;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

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

	private final CookieProvider cookieProvider;

	private final Redirection redirection;

	@DeleteMapping("/users/deactivate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void withdraw(@AuthUser final User user, final HttpServletResponse response) throws IOException {
		userService.withdraw(user);
		cookieProvider.clearAuthCookie(response);
		redirection.to("/", response);
	}

	@GetMapping("/users/me")
	public ResponseEntity<BaseApiResponse<UserResponse>> fetchMe(@AuthUser final User user) {
		final UserResponse response = UserResponse.from(user);
		return ResponseEntity.ok(BaseApiResponse.success("사용자 조회 성공", response));
	}

	@PostMapping("/users/me/nickname")
	public ResponseEntity<BaseApiResponse<ChangeUserNicknameResponse>> changeNickname(@AuthUser final User user,
																					  @RequestBody @Valid final ChangeUserNicknameRequest changeNickname) {

		try {
			final ChangeUserNicknameResponse response = changeNicknameService.changeUserNickname(user,
					changeNickname.changeNickname());

			return ResponseEntity.ok(BaseApiResponse.success("닉네임 변경 성공", response));
		} catch (BusinessException e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(BaseApiResponse.fail(e.getMessage(), null));
		}
	}
}


