package com.ludo.study.studymatchingplatform.auth.google.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.google.service.GoogleSignUpService;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.dto.response.SignupResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/signup")
public class GoogleSignUpController {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final GoogleSignUpService googleSignUpService;

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
			@RequestParam(name = "code") final String authorizationCode) {
		SignupResponse signupResponse = googleSignUpService.googleSignUp(authorizationCode);

		return ResponseEntity.ok()
				.body(signupResponse);
	}
}
