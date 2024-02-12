package com.ludo.study.studymatchingplatform.auth.google.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.google.GoogleOAuthToken;
import com.ludo.study.studymatchingplatform.auth.google.GoogleUserInfo;
import com.ludo.study.studymatchingplatform.auth.naver.service.dto.response.SignupResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleSignUpService {

	private final GoogleOAuthTokenRequestService googleOAuthTokenRequestService;
	private final GoogleProfileRequestService googleProfileRequestService;
	private final UserRepositoryImpl userRepository;

	@Transactional
	public SignupResponse googleSignUp(final String authorizationCode) {
		final GoogleOAuthToken oAuthToken = googleOAuthTokenRequestService.createOAuthToken(authorizationCode, true);
		final GoogleUserInfo userInfo = googleProfileRequestService.createGoogleUserInfo(oAuthToken.getAccessToken());

		validateAlreadySignUp(userInfo);

		final User user = userInfo.toUser();
		userRepository.save(user);

		return new SignupResponse(true, "회원가입을 완료했습니다.");
	}

	private void validateAlreadySignUp(final GoogleUserInfo userInfo) {
		userRepository.findByEmail(userInfo.getEmail())
				.ifPresent(user -> {
					throw new IllegalArgumentException("이미 가입되어 있는 회원입니다.");
				});
	}

}
