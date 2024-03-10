package com.ludo.study.studymatchingplatform.auth.controller.google;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.google.GoogleSignUpService;
import com.ludo.study.studymatchingplatform.auth.service.naver.dto.response.SignupResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/signup")
public class GoogleSignUpController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final GoogleSignUpService googleSignUpService;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;

	@GetMapping("/google")
	public String googleSignup(final RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute(
				"response_type", "code");
		redirectAttributes.addAttribute(
				"client_id", clientRegistrationAndProviderRepository.findClientId(Social.GOOGLE));
		redirectAttributes.addAttribute(
				"redirect_uri", clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.GOOGLE));
		// redirectAttributes.addAttribute("scope",
		// 		"https://www.googleapis.com/auth/userinfo.email,https://www.googleapis.com/auth/userinfo.profile");
		redirectAttributes.addAttribute("scope", "email profile");

		return "redirect:" + clientRegistrationAndProviderRepository.findAuthorizationUri(Social.GOOGLE);
	}

	@GetMapping("/google/callback")
	public ResponseEntity<SignupResponse> googleSignupback(
			@RequestParam(name = "code") final String authorizationCode, final HttpServletResponse response) {
		final User user = googleSignUpService.googleSignUp(authorizationCode);
		final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));
		cookieProvider.setAuthCookie(accessToken, response);

		return ResponseEntity.ok()
				.body(new SignupResponse(true, "회원 가입에 성공했습니다."));
	}

}
