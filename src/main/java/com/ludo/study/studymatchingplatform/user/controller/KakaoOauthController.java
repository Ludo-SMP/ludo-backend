package com.ludo.study.studymatchingplatform.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.user.service.KakaoOauthService;
import com.ludo.study.studymatchingplatform.user.service.UserJoinService;
import com.ludo.study.studymatchingplatform.user.service.dto.response.AuthenticationResponse;
import com.ludo.study.studymatchingplatform.user.service.dto.response.KakaoOauthRedirectResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class KakaoOauthController {

	private final KakaoOauthService kakaoOauthService;
	private final UserJoinService userJoinService;

	// @GetMapping("/signup/kakao/callback")
	// public void signupCallback(@RequestParam("code") String code) {
	// 	log.info("인가 코드를 이용하여 토큰을 받습니다.");
	// 	final KakaoTokenResponse kakaoTokenResponse = kakaoOauthService.getToken(code);
	// 	final UserCreateDto userCreateDto = kakaoOauthService.getUserInfo(kakaoTokenResponse.access_token());
	// 	userService.create(userCreateDto);
	// }

	@GetMapping("/signup/kakao")
	public ResponseEntity<KakaoOauthRedirectResponse> signupOauth() {
		final KakaoOauthRedirectResponse kakaoOauthRedirectResponse = kakaoOauthService.makeSignupOauthUrl();
		return ResponseEntity.ok(kakaoOauthRedirectResponse);
	}

	@GetMapping("/signup/kakao/callback")
	public ResponseEntity<AuthenticationResponse> signupOauth(
			@RequestParam(value = "code") final String code) {
		final Map<String, String> queryParams = Map.of(
				"code", code
		);
		final AuthenticationResponse response = kakaoOauthService.signup(queryParams);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/login/kakao")
	public ResponseEntity<KakaoOauthRedirectResponse> loginOauth() {
		final KakaoOauthRedirectResponse kakaoOauthRedirectResponse = kakaoOauthService.makeLoginOauthUrl();
		return ResponseEntity.ok(kakaoOauthRedirectResponse);
	}

	@GetMapping("/login/kakao/callback")
	public ResponseEntity<AuthenticationResponse> loginOauth(
			@RequestParam(value = "code") final String code) {
		final Map<String, String> queryParams = Map.of(
				"code", code
		);
		final AuthenticationResponse response = kakaoOauthService.login(queryParams);
		return ResponseEntity.ok(response);
	}

}
