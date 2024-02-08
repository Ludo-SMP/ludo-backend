package com.ludo.study.studymatchingplatform.auth.naver.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.auth.naver.service.vo.response.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.response.UserProfile;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverLoginService {

	private final NaverOAuthTokenRequestService naverOAuthTokenRequestService;
	private final NaverProfileRequestService naverProfileRequestService;
	private final UserRepositoryImpl userRepository;

	public void login(final String authorizationCode) {
		final NaverOAuthToken oAuthToken = naverOAuthTokenRequestService.createOAuthToken(authorizationCode);
		final UserProfile profileResponse = naverProfileRequestService.createNaverProfile(oAuthToken);
		validateNotSignUp(profileResponse);

		// TODO: 로그인 상태 유지 로직 구현 (session vs jwt)
		// TODO: 로그인 API response 응답
	}

	private void validateNotSignUp(final UserProfile profileResponse) {
		Optional<User> findUser = userRepository.findByEmail(profileResponse.getEmail());
		if (findUser.isEmpty()) {
			throw new IllegalArgumentException("가입되어 있지 않은 회원입니다.");
		}
	}

}
