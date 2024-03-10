package com.ludo.study.studymatchingplatform.auth.controller.google;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;

@RestController
public class AuthTestController {

	@GetMapping("/test")
	public AuthUserPayload test(@AuthUser AuthUserPayload payload) {
		System.out.println(payload);
		return payload;
	}
}
