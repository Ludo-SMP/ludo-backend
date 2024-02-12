package com.ludo.study.studymatchingplatform.auth.kakao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.KakaoSignUpResponse;
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
	public KakaoSignUpResponse kakaoSignUp(final String authorizationCode) {
		final String kakaoSignUpRedirectUri =
				clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.KAKAO);
		final KakaoOAuthToken kakaoOAuthToken =
				kakaoOAuthTokenRequestService.createOAuthToken(authorizationCode, kakaoSignUpRedirectUri);
		final KakaoUserProfileDto kakaoUserProfileDto = kakaoProfileRequestService.createKakaoProfile(kakaoOAuthToken);
		signUp(kakaoUserProfileDto);
		return new KakaoSignUpResponse(true, "회원가입을 완료했습니다.", kakaoOAuthToken.getAccessToken());
	}

	private void signUp(final KakaoUserProfileDto kakaoUserProfileDto) {
		User user = kakaoUserProfileDto.toUser();
		userRepository.save(user);
	}

}
