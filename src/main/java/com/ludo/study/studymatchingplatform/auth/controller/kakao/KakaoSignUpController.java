package com.ludo.study.studymatchingplatform.auth.controller.kakao;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.KakaoSignUpService;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;

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

	@GetMapping("/kakao")
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
	public void kakaoSignUpCallback(
			@RequestParam(name = "code") String authorizationCode,
			final HttpServletResponse response) throws IOException {
		kakaoSignUpService.kakaoSignUp(authorizationCode, response);
		response.sendRedirect("http://localhost:3000");
	}

}
