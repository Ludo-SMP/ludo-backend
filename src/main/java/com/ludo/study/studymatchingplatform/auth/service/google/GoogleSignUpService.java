package com.ludo.study.studymatchingplatform.auth.service.google;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleUserInfo;
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
	public User googleSignUp(final String authorizationCode) {
		final GoogleOAuthToken oAuthToken = googleOAuthTokenRequestService.createOAuthToken(authorizationCode, true);
		final GoogleUserInfo userInfo = googleProfileRequestService.createGoogleUserInfo(oAuthToken.getAccessToken());

		validateAlreadySignUp(userInfo);

		return signup(userInfo);
	}

	private void validateAlreadySignUp(final GoogleUserInfo userInfo) {
		userRepository.findByEmail(userInfo.getEmail())
				.ifPresent(user -> {
					throw new IllegalArgumentException("이미 가입되어 있는 회원입니다.");
				});
	}

	private User signup(final GoogleUserInfo userInfo) {
		final User user = userRepository.save(userInfo.toUser());
		user.setInitialDefaultNickname();
		return user;
	}

}
