package com.ludo.study.studymatchingplatform.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.builder.AuthBuilder;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.AuthenticationResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.service.dto.OauthUserJoinDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserJoinService {

	private final UserRepositoryImpl userRepository;

	@Transactional
	public AuthenticationResponse oauthJoin(final OauthUserJoinDto oauthUserJoinDto) {
		final User user = joinUser(oauthUserJoinDto);
		userRepository.save(user);
		return makeAuthenticationResponse(user);
	}

	public User joinUser(final OauthUserJoinDto oauthUserJoinDto) {
		return new User(
				oauthUserJoinDto.social(),
				oauthUserJoinDto.nickname(), oauthUserJoinDto.email(),
				oauthUserJoinDto.refreshToken(), oauthUserJoinDto.accessToken()
		);
	}

	private AuthenticationResponse makeAuthenticationResponse(final User user) {
		return AuthBuilder.convertToAuthenticationResponse(user.getRefreshToken(), user.getAccessToken());
	}

}
