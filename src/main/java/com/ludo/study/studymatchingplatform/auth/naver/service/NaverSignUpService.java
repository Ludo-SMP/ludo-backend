package com.ludo.study.studymatchingplatform.auth.naver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.naver.service.dto.response.SignupResponse;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.response.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.response.UserProfile;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverSignUpService {

	private final NaverOAuthTokenRequestService naverOAuthTokenRequestService;
	private final NaverProfileRequestService naverProfileRequestService;
	private final UserRepositoryImpl userRepository;

	@Transactional
	public SignupResponse naverSignUp(final String authorizationCode) {
		final NaverOAuthToken oAuthToken = naverOAuthTokenRequestService.createOAuthToken(authorizationCode);
		final UserProfile userProfile = naverProfileRequestService.createNaverProfile(oAuthToken);

		validateAlreadySignUp(userProfile);
		signup(userProfile);

		return new SignupResponse(true, "회원가입을 완료했습니다.");
	}

	private void validateAlreadySignUp(final UserProfile userProfile) {
		userRepository.findByEmail(userProfile.getEmail())
				.ifPresent(user -> {
					throw new IllegalArgumentException("이미 가입되어 있는 회원입니다.");
				});
	}

	private void signup(final UserProfile userProfile) {
		User user = userProfile.toUser();
		userRepository.save(user);
	}

}
