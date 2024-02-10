package com.ludo.study.studymatchingplatform.study.fixture;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class StudyFixture {

	public static Study createStudy(StudyStatus studyStatus, String title, Way way, Category category, User user) {
		return Study.builder()
				.status(studyStatus)
				.category(category)
				.owner(user)
				.title(title)
				.way(way)
				.startDateTime(LocalDateTime.now())
				.endDateTime(LocalDateTime.now())
				.build();
	}

}
