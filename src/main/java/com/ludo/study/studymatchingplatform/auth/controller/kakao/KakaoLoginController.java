package com.ludo.study.studymatchingplatform.auth.controller.kakao;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.KakaoLoginService;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

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
	private final CookieProvider cookieProvider;

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
	public void kakaoLoginCallback(
			@RequestParam(name = "code") String authorizationCode,
			final HttpServletResponse response) throws IOException {
		final User user = kakaoLoginService.login(authorizationCode);
		final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));
		cookieProvider.setAuthCookie(accessToken, response);
		// response.sendRedirect("https://ludoapi.store");
		response.sendRedirect("http://localhost:3000");
	}

}
