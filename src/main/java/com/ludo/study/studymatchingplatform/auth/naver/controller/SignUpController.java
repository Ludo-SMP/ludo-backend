package com.ludo.study.studymatchingplatform.auth.naver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.NaverSignUpService;
import com.ludo.study.studymatchingplatform.auth.naver.service.dto.response.SignupResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/signup")
public class SignUpController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final NaverSignUpService naverSignUpService;

	@GetMapping("/naver")
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
	public ResponseEntity<SignupResponse> naverSignupback(@RequestParam(name = "code") String authorizationCode) {
		SignupResponse signupResponse = naverSignUpService.naverSignUp(authorizationCode);

		return ResponseEntity.ok()
			.body(signupResponse);
	}
}
