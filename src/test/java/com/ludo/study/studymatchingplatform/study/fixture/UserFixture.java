package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class UserFixture {

	public static User createUser(Social social, String nickname, String email) {
		return User.builder()
				.social(social)
				.nickname(nickname)
				.email(email)
				.build();
	}

}
