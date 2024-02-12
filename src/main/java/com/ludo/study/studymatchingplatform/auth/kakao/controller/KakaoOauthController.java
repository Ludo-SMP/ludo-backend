package com.ludo.study.studymatchingplatform.auth.kakao.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.kakao.service.KakaoOauthService;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.AuthenticationResponse;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.KakaoOauthRedirectResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class KakaoOauthController {

	private final KakaoOauthService kakaoOauthService;

	@GetMapping("/signup/kakao")
	public ResponseEntity<KakaoOauthRedirectResponse> signupOauth() {
		final KakaoOauthRedirectResponse kakaoOauthRedirectResponse = kakaoOauthService.makeSignupOauthUrl();
		return ResponseEntity.ok(kakaoOauthRedirectResponse);
	}

	@GetMapping("/signup/kakao/callback")
	public ResponseEntity<Void> signupOauth(
			@RequestParam(value = "code") final String code) {
		final Map<String, String> queryParams = Map.of(
				"code", code
		);
		kakaoOauthService.signup(queryParams);
		return ResponseEntity.ok(null);
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
