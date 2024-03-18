package com.ludo.study.studymatchingplatform.auth.service.kakao;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.domain.user.redis.RefreshToken;
import com.ludo.study.studymatchingplatform.user.repository.user.redis.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoRefreshService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final KakaoOAuthTokenRequestService kakaoOAuthTokenRequestService;
	private final JwtTokenProvider jwtTokenProvider;

	public String refreshToRDB(final User user) {
		final KakaoOAuthToken token = kakaoOAuthTokenRequestService.createRefreshOAuthToken(user.getRefresh());
		if (token.getRefreshToken() != null) {
			user.changeRefreshToken(token.getRefreshToken());
		}
		return jwtTokenProvider.createAccessToken(AuthUserPayload.from(user, token.getAccessToken()));
	}

	public String refreshToRedis(final User user) {
		final RefreshToken refreshToken = refreshTokenRepository.findById(user.getId())
				.orElseThrow(() -> new NotFoundException("존재하지 않는 토큰 입니다."));
		final KakaoOAuthToken token = kakaoOAuthTokenRequestService.createRefreshOAuthToken(refreshToken.getToken());
		if (token.getRefreshToken() != null) {
			user.changeRefreshToken(token.getRefreshToken());
		}
		return jwtTokenProvider.createAccessToken(AuthUserPayload.from(user, token.getAccessToken()));
	}

}
