package com.ludo.study.studymatchingplatform.auth.service.kakao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.domain.user.redis.RefreshToken;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.repository.user.redis.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoSignUpService {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final KakaoOAuthTokenRequestService kakaoOAuthTokenRequestService;
	private final KakaoProfileRequestService kakaoProfileRequestService;
	private final UserRepositoryImpl userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void kakaoSignUp(final String authorizationCode, final HttpServletResponse response) {
		final String kakaoSignUpRedirectUri =
				clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.KAKAO);
		final KakaoOAuthToken kakaoOAuthToken =
				kakaoOAuthTokenRequestService.createOAuthToken(authorizationCode, kakaoSignUpRedirectUri);
		final KakaoUserProfileDto kakaoUserProfileDto = kakaoProfileRequestService.createKakaoProfile(kakaoOAuthToken);
		validateAlreadySignUp(kakaoUserProfileDto);
		final User user = sinup(kakaoUserProfileDto, kakaoOAuthToken);
		final String accessToken =
				jwtTokenProvider.createAccessToken(AuthUserPayload.from(user, kakaoOAuthToken.getRefreshToken()));
		cookieProvider.setAuthCookie(accessToken, response);
	}

	private void validateAlreadySignUp(final KakaoUserProfileDto userInfo) {
		userRepository.findByEmail(userInfo.getEmail())
				.ifPresent(user -> {
					throw new IllegalArgumentException("이미 가입되어 있는 회원입니다.");
				});
	}

	private User sinup(KakaoUserProfileDto kakaoUserProfileDto, final KakaoOAuthToken token) {
		final User user = userRepository.save(kakaoUserProfileDto.toUser(token.getRefreshToken()));
		final RefreshToken refreshToken = new RefreshToken(user.getId(), token.getRefreshToken(), token.getExpiresIn());
		refreshTokenRepository.save(refreshToken);
		user.setInitialDefaultNickname();
		return user;
	}

}
