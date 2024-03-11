package com.ludo.study.studymatchingplatform.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ludo.study.studymatchingplatform.user.domain.exception.UserExceptionMessage;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;

class UserTest {

	@Test
	@DisplayName("[Success] 사용자 초기 닉네임 설정 성공")
	void initDefaultNickname() {
		User user = UserFixture.createUserWithId(1L, Social.NAVER, "archa", "archa@naver.com");
		user.setInitialDefaultNickname();

		assertThat(user.getNickname())
				.isEqualTo(String.format(User.DEFAULT_NICKNAME_PREFIX, User.DEFAULT_NICKNAME_ID + 1L));
	}

	@Test
	@DisplayName("[Exception] 사용자 아이디가 없을 경우 초기 닉네임 설정 실패")
	void useridIsNull() {
		User user = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");

		assertThatThrownBy(user::setInitialDefaultNickname)
				.hasMessage(UserExceptionMessage.INIT_DEFAULT_NICKNAME.getMessage());
	}

}
