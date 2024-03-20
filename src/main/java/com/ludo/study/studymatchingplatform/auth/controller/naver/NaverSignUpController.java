package com.ludo.study.studymatchingplatform.auth.controller.naver;

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
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.naver.NaverSignUpService;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/signup")
public class NaverSignUpController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final NaverSignUpService naverSignUpService;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;

	@GetMapping("/naver")
	@ResponseStatus(HttpStatus.FOUND)
	public String naverSignup(RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute(
				"response_type", "code");
		redirectAttributes.addAttribute(
				"client_id", clientRegistrationAndProviderRepository.findClientId(Social.NAVER));
		redirectAttributes.addAttribute(
				"redirect_uri", clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.NAVER));

		return "redirect:" + clientRegistrationAndProviderRepository.findAuthorizationUri(Social.NAVER);
	}

	@GetMapping("/naver/callback")
	@ResponseStatus(HttpStatus.FOUND)
	@DataFieldName("user")
	public void naverSignupCallback(
			@RequestParam(name = "code") final String authorizationCode,
			final HttpServletResponse response) throws IOException {

		final User user = naverSignUpService.naverSignUp(authorizationCode);
		final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));

		cookieProvider.setAuthCookie(accessToken, response);
		response.sendRedirect("https://ludoapi.store");
	}
}
