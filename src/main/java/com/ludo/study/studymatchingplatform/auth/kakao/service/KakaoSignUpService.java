package com.ludo.study.studymatchingplatform.auth.kakao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoSignUpService {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final KakaoOAuthTokenRequestService kakaoOAuthTokenRequestService;
	private final KakaoProfileRequestService kakaoProfileRequestService;
	private final UserRepositoryImpl userRepository;

	@Transactional
	public User kakaoSignUp(final String authorizationCode) {
		final String kakaoSignUpRedirectUri =
				clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.KAKAO);
		final KakaoOAuthToken kakaoOAuthToken =
				kakaoOAuthTokenRequestService.createOAuthToken(authorizationCode, kakaoSignUpRedirectUri);
		final KakaoUserProfileDto kakaoUserProfileDto = kakaoProfileRequestService.createKakaoProfile(kakaoOAuthToken);

		validateAlreadySignUp(kakaoUserProfileDto);

		User user = kakaoUserProfileDto.toUser();
		userRepository.save(user);
		return user;
	}

	private void validateAlreadySignUp(final KakaoUserProfileDto userInfo) {
		userRepository.findByEmail(userInfo.getEmail())
				.ifPresent(user -> {
					throw new IllegalArgumentException("이미 가입되어 있는 회원입니다.");
				});
	}

}
