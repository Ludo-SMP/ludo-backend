package com.ludo.study.studymatchingplatform.auth.service.naver;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.auth.service.naver.dto.response.LoginResponse;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.UserProfile;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverLoginService {

	private final NaverOAuthTokenRequestService naverOAuthTokenRequestService;
	private final NaverProfileRequestService naverProfileRequestService;
	private final UserRepositoryImpl userRepository;

	public LoginResponse login(final String authorizationCode) {
		final NaverOAuthToken oAuthToken = naverOAuthTokenRequestService.createOAuthToken(authorizationCode);
		final UserProfile profileResponse = naverProfileRequestService.createNaverProfile(oAuthToken);

		final User user = validateNotSignUp(profileResponse);

		return new LoginResponse(
				String.valueOf(user.getId()),
				user.getNickname(),
				user.getEmail());
	}

	private User validateNotSignUp(final UserProfile profileResponse) {
		return userRepository.findByEmail(profileResponse.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("가입되어 있지 않은 회원입니다."));
	}

}
