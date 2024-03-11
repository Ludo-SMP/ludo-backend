package com.ludo.study.studymatchingplatform.auth.service.kakao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoLoginService {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final KakaoOAuthTokenRequestService kakaoOAuthTokenRequestService;
	private final KakaoProfileRequestService kakaoProfileRequestService;
	private final UserRepositoryImpl userRepository;

	public User login(final String authrizationCode) {
		final String kakaoLoginRedirectUri = clientRegistrationAndProviderRepository.findLoginRedirectUri(Social.KAKAO);
		final KakaoOAuthToken kakaoOAuthToken =
				kakaoOAuthTokenRequestService.createOAuthToken(authrizationCode, kakaoLoginRedirectUri);
		final KakaoUserProfileDto kakaoUserProfileDto = kakaoProfileRequestService.createKakaoProfile(kakaoOAuthToken);

		return validateNotSignUp(kakaoUserProfileDto);
	}

	private User validateNotSignUp(final KakaoUserProfileDto kakaoUserProfileDto) {
		return userRepository.findByEmail(kakaoUserProfileDto.getEmail())
				.orElseThrow(() -> new NotFoundException("가입되어 있지 않은 회원입니다"));
	}

}
