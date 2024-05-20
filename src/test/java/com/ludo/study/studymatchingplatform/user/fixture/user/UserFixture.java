package com.ludo.study.studymatchingplatform.user.fixture.user;

import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public class UserFixture {

	public static final User user1 = User.builder()
			.social(Social.NAVER)
			.nickname("user1")
			.email("user1@naver.com")
			.build();

	public static User createUser(Social social, String nickname, String email) {
		return User.builder()
				.social(social)
				.nickname(nickname)
				.email(email)
				.build();
	}

	public static User createUserWithId(Long userId, Social social, String nickname, String email) {
		return User.builder()
				.id(userId)
				.social(social)
				.nickname(nickname)
				.email(email)
				.build();
	}

}
