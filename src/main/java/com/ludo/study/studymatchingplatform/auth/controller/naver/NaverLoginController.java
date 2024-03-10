package com.ludo.study.studymatchingplatform.auth.controller.naver;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.naver.NaverLoginService;
import com.ludo.study.studymatchingplatform.auth.service.naver.dto.response.LoginResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class NaverLoginController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final NaverLoginService naverLoginService;
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/naver")
	public String naverLogin(RedirectAttributes redirectAttributes) {

		redirectAttributes.addAttribute(
				"response_type", "code");
		redirectAttributes.addAttribute(
				"client_id", clientRegistrationAndProviderRepository.findClientId(Social.NAVER));
		redirectAttributes.addAttribute(
				"redirect_uri", clientRegistrationAndProviderRepository.findLoginRedirectUri(Social.NAVER));

		return "redirect:" + clientRegistrationAndProviderRepository.findAuthorizationUri(Social.NAVER);
	}

	@GetMapping("/naver/callback")
	public ResponseEntity<LoginResponse> naverLoginCallback(
			@RequestParam(name = "code") String authorizationCode,
			HttpServletResponse response
	) {
		LoginResponse loginResponse = naverLoginService.login(authorizationCode);

		String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(loginResponse.id()));

		Cookie cookie = new Cookie("Authorization", accessToken);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.parseInt(jwtTokenProvider.getAccessTokenExpiresIn()));
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		return ResponseEntity.ok()
				.body(loginResponse);
	}

}
