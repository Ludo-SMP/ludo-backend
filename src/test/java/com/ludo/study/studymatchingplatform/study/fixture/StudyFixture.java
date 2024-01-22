package com.ludo.study.studymatchingplatform.study.fixture;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class StudyFixture {

	public static Study createStudy(String title, Way way, User user, Category category) {
		return Study.builder()
			.title(title)
			.category(category)
			.owner(user)
			.way(way)
			.startDateTime(LocalDateTime.now())
			.endDateTime(LocalDateTime.now())
			.createdDateTime(LocalDateTime.now())
			.build();
	}

}
