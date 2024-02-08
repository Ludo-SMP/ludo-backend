package com.ludo.study.studymatchingplatform.auth.naver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.naver.repository.OAuthProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.NaverSignUpService;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/signup")
public class SignUpController {

	private final OAuthProviderRepository oAuthProviderRepository;
	private final NaverSignUpService naverSignUpService;

	@GetMapping("/naver")
	public String naverSignup(RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("response_type", "code");
		redirectAttributes.addAttribute("client_id", oAuthProviderRepository.findClientId(Social.NAVER));
		redirectAttributes.addAttribute("redirect_uri", oAuthProviderRepository.findSignupRedirectUri(Social.NAVER));

		return "redirect:" + oAuthProviderRepository.findAuthorizationUri(Social.NAVER);
	}

	@GetMapping("/naver/callback")
	public String naverSignupback(@RequestParam(name = "code") String authorizationCode) {
		naverSignUpService.naverSignUp(authorizationCode);

		// TODO: API 응답 명세에 맞게 수정
		return "signup success";
	}
}
