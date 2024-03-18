package com.ludo.study.studymatchingplatform.auth.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;

@SpringBootTest
class JwtTokenProviderTest {

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Test
	void jwt_access_token_생성() {
		AuthUserPayload payload = AuthUserPayload.from(1L, "access");
		String accessToken = jwtTokenProvider.createAccessToken(payload);
		assertThat(accessToken).isNotNull();
	}

	@Test
	void jwt_access_token_검증() {
		AuthUserPayload payload = AuthUserPayload.from(1L, "access");
		String accessToken = jwtTokenProvider.createAccessToken(payload);

		assertThatCode(() -> jwtTokenProvider.verifyAuthTokenOrThrow(accessToken))
				.doesNotThrowAnyException();
	}

}
