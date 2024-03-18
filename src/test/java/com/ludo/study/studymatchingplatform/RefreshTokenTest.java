package com.ludo.study.studymatchingplatform;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ludo.study.studymatchingplatform.user.domain.user.redis.RefreshToken;
import com.ludo.study.studymatchingplatform.user.repository.user.redis.RefreshTokenRepository;

@SpringBootTest
class RefreshTokenTest {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@AfterEach
	void tearDown() throws Exception {
		refreshTokenRepository.deleteAll();
	}

	@DisplayName("[Success] 리프레시 토큰 등록 조회 태스트")
	@Test
	void redisConnectionTest() {
		// given
		final Long id = 1L;
		RefreshToken refreshToken = new RefreshToken(id, "refreshToken", 12345556);

		// when
		refreshTokenRepository.save(refreshToken);

		// then
		RefreshToken savedRefreshToken = refreshTokenRepository.findById(id).get();
		assertThat(savedRefreshToken.getUserId()).isEqualTo(1L);
		assertThat(savedRefreshToken.getToken()).isEqualTo("refreshToken");
	}

}
