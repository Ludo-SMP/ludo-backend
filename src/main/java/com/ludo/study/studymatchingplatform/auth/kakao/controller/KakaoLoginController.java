package com.ludo.study.studymatchingplatform.auth.kakao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.common.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.kakao.service.KakaoLoginService;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.KakaoLoginResponse;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/login")
public class KakaoLoginController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final KakaoLoginService kakaoLoginService;
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/kakao")
	public String kakaoLogin(RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute(
				"response_type", "code");
		redirectAttributes.addAttribute(
				"client_id", clientRegistrationAndProviderRepository.findClientId(Social.KAKAO));
		redirectAttributes.addAttribute(
				"redirect_uri", clientRegistrationAndProviderRepository.findLoginRedirectUri(Social.KAKAO));
		return "redirect:" + clientRegistrationAndProviderRepository.findAuthorizationUri(Social.KAKAO);
	}

	@GetMapping("/kakao/callback")
	public ResponseEntity<KakaoLoginResponse> kakaoLoginCallback(@RequestParam(name = "code") String authorizationCode,
																 HttpServletResponse response) {
		KakaoLoginResponse kakaoLoginResponse = kakaoLoginService.login(authorizationCode);

		Cookie cookie = new Cookie("Authorization", kakaoLoginResponse.assessToken());
		cookie.setPath("/");
		cookie.setMaxAge(Integer.parseInt(jwtTokenProvider.getAccessTokenExpiresIn()));
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		return ResponseEntity.ok().body(kakaoLoginResponse);
	}

}
