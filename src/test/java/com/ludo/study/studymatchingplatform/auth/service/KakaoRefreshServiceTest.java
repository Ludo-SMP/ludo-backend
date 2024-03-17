package com.ludo.study.studymatchingplatform.auth.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.dto.KakaoOAuthTokenTestResponse;
import com.ludo.study.studymatchingplatform.auth.service.kakao.KakaoOAuthTokenRequestService;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.domain.user.redis.RefreshToken;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.repository.user.redis.RefreshTokenRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KakaoRefreshServiceTest {

	@Autowired
	private KakaoOAuthTokenRequestService kakaoOAuthTokenRequestService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;

	@Autowired
	private UserRepositoryImpl userRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@AfterEach
	void tearDown() throws Exception {
		refreshTokenRepository.deleteAll();
		userRepository.deleteAll();
	}

	@DisplayName("[Success] 10000명의 사용자를 생성한다.")
	@Test
	@Order(1)
	void createUsersTest() {
		for (int i = 1; i <= 10000; i++) {
			final User user = UserFixture.createUserWithId((long)i, Social.KAKAO, "닉네임" + i,
					"lee@kakao.com", "리프레시" + i);
			final RefreshToken refreshToken = new RefreshToken((long)i, "리프레시" + i, 123435254L);
			refreshTokenRepository.save(refreshToken);
			userRepository.save(user);
		}
	}

	@DisplayName("[Success] 10000명의 사용자가 RDB 를 통한 토큰 갱신을 요청한다.")
	@Test
	@Order(2)
	void usersRefreshToRDBTest() {
		for (int i = 1; i <= 10000; i++) {
			final User user = userRepository.findById((long)i)
					.orElseThrow(() -> new NotFoundException("존재 하지 않는 사용자 입니다."));
			refreshToRDB(user);
		}
	}

	@DisplayName("[Success] 10000명의 사용자가 Redis 를 통한 토큰 갱신을 요청한다.")
	@Test
	@Order(3)
	void usersRefreshToRedisTest() {
		for (int i = 1; i <= 10000; i++) {
			final User user = userRepository.findById((long)i)
					.orElseThrow(() -> new NotFoundException("존재 하지 않는 사용자 입니다."));
			refreshToRedis(user);
		}
	}

	@DisplayName("[Success] 1명의 사용자가 RDB 를 통한 토큰 갱신을 10000번 요청한다.")
	@Test
	@Order(4)
	void userRefreshToRDBTest() {
		final User user = userRepository.findById(1L)
				.orElseThrow(() -> new NotFoundException("존재 하지 않는 사용자 입니다."));
		for (int i = 1; i <= 10000; i++) {
			refreshToRDB(user);
		}
	}

	@DisplayName("[Success] 1명의 사용자가 Redis 를 통한 토큰 갱신을 10000번 요청한다.")
	@Test
	@Order(5)
	void userRefreshToRedisTest() {
		final User user = userRepository.findById(1L)
				.orElseThrow(() -> new NotFoundException("존재 하지 않는 사용자 입니다."));
		for (int i = 1; i <= 10000; i++) {
			refreshToRedis(user);
		}
	}

	private String refreshToRDB(final User user) {
		final KakaoOAuthTokenTestResponse token = createRefreshOAuthToken(user.getRefresh());
		if (token != null) {
			user.changeRefreshToken(token.refreshToken());
			userRepository.save(user);
		}
		return jwtTokenProvider.createAccessToken(AuthUserPayload.from(user, token.accessToken()));
	}

	private String refreshToRedis(final User user) {
		final RefreshToken refreshToken = refreshTokenRepository.findById(user.getId())
				.orElseThrow(() -> new NotFoundException("존재하지 않는 토큰 입니다."));
		final KakaoOAuthTokenTestResponse token = createRefreshOAuthToken(refreshToken.getToken());
		if (token.refreshToken() != null) {
			refreshToken.update(token.refreshToken(), 213152345L);
			refreshTokenRepository.save(refreshToken);
		}
		return jwtTokenProvider.createAccessToken(AuthUserPayload.from(user, token.refreshToken()));
	}

	private KakaoOAuthTokenTestResponse createRefreshOAuthToken(final String refreshToken) {
		final String tokenUri = clientRegistrationAndProviderRepository.findTokenUri(Social.KAKAO);
		final HttpHeaders headers = createHeaders();
		final MultiValueMap<String, String> body = createRefreshBody(refreshToken);
		return new KakaoOAuthTokenTestResponse("tokenType", "accessToken", "refreshToken", 12355);
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}

	private MultiValueMap<String, String> createRefreshBody(final String refreshToken) {
		final String clientId = clientRegistrationAndProviderRepository.findClientId(Social.KAKAO);
		final String clientSecret = clientRegistrationAndProviderRepository.findClientSecret(Social.KAKAO);
		final String grantType = clientRegistrationAndProviderRepository.findRefreshGrantType(Social.KAKAO);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", grantType);
		body.add("client_id", clientId);
		body.add("refresh_token", refreshToken);
		body.add("client_secret", clientSecret);
		return body;
	}

}
