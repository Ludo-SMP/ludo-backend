package com.ludo.study.studymatchingplatform.auth.controller.kakao;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.common.service.UserDetailsService;
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.KakaoLoginService;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.common.properties.ClientProperties;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/login")
public class KakaoLoginController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final ClientProperties clientProperties;

	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;

	private final KakaoLoginService kakaoLoginService;
	private final UserDetailsService userDetailsService;

	@GetMapping("/kakao")
	@ResponseStatus(HttpStatus.FOUND)
	public String kakaoLogin(RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute(
				"response_type", "code");
		redirectAttributes.addAttribute(
				"client_id", clientRegistrationAndProviderRepository.findClientId(Social.KAKAO));
		redirectAttributes.addAttribute(
				"redirect_uri", clientRegistrationAndProviderRepository.findLoginRedirectUri(Social.KAKAO));
		return "redirect:" + clientRegistrationAndProviderRepository.findAuthorizationUri(Social.KAKAO);
	}

	@DataFieldName("user")
	@GetMapping("/kakao/callback")
	@ResponseStatus(HttpStatus.FOUND)
	public void kakaoLoginCallback(
			@RequestParam(name = "code") String authorizationCode,
			final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		final User user = kakaoLoginService.login(authorizationCode);
		final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));
		// userDetailsService.createUserDetails(user, request);
		cookieProvider.setAuthCookie(accessToken, response);
		response.sendRedirect(clientProperties.getUrl());
	}

}
