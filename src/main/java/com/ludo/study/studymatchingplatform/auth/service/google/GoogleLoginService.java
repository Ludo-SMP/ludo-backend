package com.ludo.study.studymatchingplatform.auth.service.google;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleUserInfo;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleLoginService {

	private final GoogleOAuthTokenRequestService googleOAuthTokenRequestService;
	private final GoogleProfileRequestService googleProfileRequestService;
	private final UserRepositoryImpl userRepository;

	public User login(final String authorizationCode) {
		final GoogleOAuthToken oAuthToken = googleOAuthTokenRequestService.createOAuthToken(authorizationCode, false);
		final GoogleUserInfo userInfo = googleProfileRequestService.createGoogleUserInfo(
				oAuthToken.getAccessToken());

		final User user = validateNotSignUp(userInfo);

		return user;
	}

	private User validateNotSignUp(final GoogleUserInfo userInfo) {
		return userRepository.findByEmail(userInfo.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("가입되어 있지 않은 회원입니다."));
	}

}
