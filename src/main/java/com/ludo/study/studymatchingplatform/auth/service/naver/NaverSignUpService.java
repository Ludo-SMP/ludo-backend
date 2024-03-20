package com.ludo.study.studymatchingplatform.auth.service.naver;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.UserProfile;
import com.ludo.study.studymatchingplatform.study.service.exception.DuplicatedSignUpException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverSignUpService {

	private final NaverOAuthTokenRequestService naverOAuthTokenRequestService;
	private final NaverProfileRequestService naverProfileRequestService;
	private final UserRepositoryImpl userRepository;

	@Transactional
	public User naverSignUp(final String authorizationCode) {
		final NaverOAuthToken oAuthToken = naverOAuthTokenRequestService.createOAuthToken(authorizationCode);
		final UserProfile userProfile = naverProfileRequestService.createNaverProfile(oAuthToken);

		validateAlreadySignUp(userProfile);
		return signup(userProfile);
	}

	private void validateAlreadySignUp(final UserProfile userProfile) {
		userRepository.findByEmail(userProfile.getEmail())
				.ifPresent(user -> {
					throw new DuplicatedSignUpException("이미 가입되어 있는 회원입니다.");
				});
	}

	private User signup(final UserProfile userProfile) {
		User user = userRepository.save(userProfile.toUser());
		user.setInitialDefaultNickname();
		return user;
	}

}
