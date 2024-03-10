package com.ludo.study.studymatchingplatform.auth.kakao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

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
		return signup(kakaoUserProfileDto);
	}

	private void validateAlreadySignUp(final KakaoUserProfileDto kakaoUserProfileDto) {
		userRepository.findByEmail(kakaoUserProfileDto.getEmail())
				.ifPresent(user -> {
					throw new IllegalArgumentException("이미 가입되어 있는 회원입니다.");
				});
	}

	private User signup(final KakaoUserProfileDto kakaoUserProfileDto) {
		final User user = kakaoUserProfileDto.toUser();
		userRepository.save(user);
		return user;
	}

}
