package com.ludo.study.studymatchingplatform.auth.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Test
	void jwt_access_token_생성() {
		String userId = "1";
		String accessToken = jwtTokenProvider.createAccessToken(userId);
		assertThat(accessToken).isNotNull();
	}

	@Test
	void jwt_access_token_검증() {
		String userId = "1";
		String accessToken = jwtTokenProvider.createAccessToken(userId);
		
		assertThatCode(() -> jwtTokenProvider.isValidToken(accessToken))
				.doesNotThrowAnyException();
	}

}