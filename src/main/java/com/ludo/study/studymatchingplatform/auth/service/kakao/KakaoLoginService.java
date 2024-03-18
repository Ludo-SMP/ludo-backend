package com.ludo.study.studymatchingplatform.auth.service.kakao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.domain.user.redis.RefreshToken;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.repository.user.redis.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final KakaoOAuthTokenRequestService kakaoOAuthTokenRequestService;
	private final KakaoProfileRequestService kakaoProfileRequestService;
	private final UserRepositoryImpl userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void login(final String authrizationCode, final HttpServletResponse response) {
		final String kakaoLoginRedirectUri = clientRegistrationAndProviderRepository.findLoginRedirectUri(Social.KAKAO);
		final KakaoOAuthToken kakaoOAuthToken =
				kakaoOAuthTokenRequestService.createOAuthToken(authrizationCode, kakaoLoginRedirectUri);
		final KakaoUserProfileDto kakaoUserProfileDto = kakaoProfileRequestService.createKakaoProfile(kakaoOAuthToken);
		final User user = validateNotSignUp(kakaoUserProfileDto);

		// 로그인시 refreshToken 갱신, 회원 가입과 로그인을 분리하며 로그인시 카카오에 code 를 받기위한 요청을 보내게 되는데,
		// 이 과정에서 새로운 refreshToken이 발급되며, 기존 token의 유효성 상실
		// (refresh와 access가 "key:value" 형식이라고 가정, 검증 필요)
		updatesRefreshToken(user, kakaoOAuthToken);

		final String accessToken =
				jwtTokenProvider.createAccessToken(AuthUserPayload.from(user, kakaoOAuthToken.getAccessToken()));
		cookieProvider.setAuthCookie(accessToken, response);
	}

	private User validateNotSignUp(final KakaoUserProfileDto kakaoUserProfileDto) {
		return userRepository.findByEmail(kakaoUserProfileDto.getEmail())
				.orElseThrow(() -> new NotFoundException("가입되어 있지 않은 회원입니다"));
	}

	private void updatesRefreshToken(final User user, final KakaoOAuthToken token) {
		final RefreshToken refreshToken = refreshTokenRepository.findById(user.getId())
				.orElseThrow(() -> new NotFoundException("존재하지 않는 토큰입니다."));
		refreshToken.update(token.getRefreshToken(), token.getExpiresIn());
		user.changeRefreshToken(token.getRefreshToken());
	}

}
