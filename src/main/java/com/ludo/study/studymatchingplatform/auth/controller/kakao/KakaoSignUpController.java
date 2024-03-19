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
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.KakaoSignUpService;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/signup")
public class KakaoSignUpController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final KakaoSignUpService kakaoSignUpService;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;

	@GetMapping("/kakao")
	@ResponseStatus(HttpStatus.FOUND)
	public String kakaoSignUp(RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute(
				"response_type", "code");
		redirectAttributes.addAttribute(
				"client_id", clientRegistrationAndProviderRepository.findClientId(Social.KAKAO));
		redirectAttributes.addAttribute(
				"redirect_uri", clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.KAKAO));
		return "redirect:" + clientRegistrationAndProviderRepository.findAuthorizationUri(Social.KAKAO);
	}

	@GetMapping("/kakao/callback")
	@DataFieldName("user")
	@ResponseStatus(HttpStatus.FOUND)
	public UserResponse kakaoSignUpCallback(
			@RequestParam(name = "code") String authorizationCode,
			final HttpServletResponse response) throws IOException {
		final User user = kakaoSignUpService.kakaoSignUp(authorizationCode);
		final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));
		final UserResponse userResponse = UserResponse.from(user);
		cookieProvider.setAuthCookie(accessToken, response);
		response.sendRedirect("https://local.ludoapi.store:3000");
		return userResponse;
	}

}
