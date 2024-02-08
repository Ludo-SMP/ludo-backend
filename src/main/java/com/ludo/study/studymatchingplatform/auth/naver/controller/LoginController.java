package com.ludo.study.studymatchingplatform.auth.naver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ludo.study.studymatchingplatform.auth.naver.repository.OAuthProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.NaverLoginService;
import com.ludo.study.studymatchingplatform.user.domain.Social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class LoginController {

	private final OAuthProviderRepository oAuthProviderRepository;
	private final NaverLoginService naverLoginService;

	@GetMapping("/naver")
	public String naverLogin(RedirectAttributes redirectAttributes) {

		redirectAttributes.addAttribute("response_type", "code");
		redirectAttributes.addAttribute("client_id", oAuthProviderRepository.findClientId(Social.NAVER));
		redirectAttributes.addAttribute("redirect_uri", oAuthProviderRepository.findLoginRedirectUri(Social.NAVER));

		return "redirect:" + oAuthProviderRepository.findAuthorizationUri(Social.NAVER);
	}

	@GetMapping("/naver/callback")
	public String naverLoginCallback(@RequestParam(name = "code") String authorizationCode) {
		naverLoginService.login(authorizationCode);

		//TODO: API 명세에 맞게 수정
		return "login success";
	}

}
