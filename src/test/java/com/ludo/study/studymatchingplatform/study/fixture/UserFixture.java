package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.user.domain.Platform;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class UserFixture {

	public static User createUser(Platform platform, String nickname, String email) {
		return User.builder()
			.platform(platform)
			.nickname(nickname)
			.email(email)
			.build();
	}

}
