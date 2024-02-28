package com.ludo.study.studymatchingplatform.study.fixture;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Platform;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class StudyFixture {

	public static Study createStudy(StudyStatus studyStatus, String title, Way way, Category category, User user,
									int participantCount, int participantLimit, Platform platform
	) {
		return Study.builder()
				.status(studyStatus)
				.platform(platform)
				.category(category)
				.owner(user)
				.title(title)
				.way(way)
				.participantCount(participantCount)
				.participantLimit(participantLimit)
				.startDateTime(LocalDateTime.now())
				.endDateTime(LocalDateTime.now())
				.build();
	}

	public static Study createStudy(String title, Category category, User user,
									int participantLimit, Platform platform
	) {
		return createStudy(
				StudyStatus.RECRUITING,
				title,
				Way.ONLINE,
				category,
				user,
				0,
				participantLimit,
				platform
		);
	}

}
