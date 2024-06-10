package com.ludo.study.studymatchingplatform.user.fixture.user;

import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public class UserFixture {

	public static User USER1() {
		return User.builder()
				.social(Social.NAVER)
				.nickname("user1")
				.email("user1@naver.com")
				.build();
	}

	public static User USER2() {
		return User.builder()
				.social(Social.KAKAO)
				.nickname("user2")
				.email("user2@kakao.com")
				.build();
	}

	public static User USER3() {
		return User.builder()
				.social(Social.GOOGLE)
				.nickname("user3")
				.email("user3@google.com")
				.build();
	}

	public static User USER4() {
		return User.builder()
				.social(Social.NAVER)
				.nickname("user4")
				.email("user4@naver.com")
				.build();
	}

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
