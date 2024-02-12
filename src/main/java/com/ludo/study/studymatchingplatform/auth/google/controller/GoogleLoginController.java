package com.ludo.study.studymatchingplatform.auth.google.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.common.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.google.service.GoogleLoginService;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.dto.response.LoginResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class GoogleLoginController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final GoogleLoginService googleLoginService;
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/google")
	public String googleLogin(RedirectAttributes redirectAttributes) {

		redirectAttributes.addAttribute(
				"response_type", "code");
		redirectAttributes.addAttribute(
				"client_id", clientRegistrationAndProviderRepository.findClientId(Social.GOOGLE));
		redirectAttributes.addAttribute(
				"redirect_uri", clientRegistrationAndProviderRepository.findLoginRedirectUri(Social.GOOGLE));
		redirectAttributes.addAttribute("scope", "email profile");

		return "redirect:" + clientRegistrationAndProviderRepository.findAuthorizationUri(Social.GOOGLE);
	}

	@GetMapping("/google/callback")
	public ResponseEntity<LoginResponse> googleLoginCallback(
			@RequestParam(name = "code") String authorizationCode,
			HttpServletResponse response
	) {
		LoginResponse loginResponse = googleLoginService.login(authorizationCode);
		String accessToken = jwtTokenProvider.createAccessToken(loginResponse.id());

		Cookie cookie = new Cookie("Authorization", accessToken);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.parseInt(jwtTokenProvider.getAccessTokenExpiresIn()));
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		return ResponseEntity.ok()
				.body(loginResponse);
	}

}
