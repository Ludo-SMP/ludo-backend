package com.ludo.study.studymatchingplatform.study.fixture.study;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;

public class StudyFixture {

	public static final Study study1 = Study.builder()
			.title("스터디1 제목")
			.owner(UserFixture.user1)
			.category(CategoryFixture.CATEGORY_PROJECT)
			.status(StudyStatus.RECRUITING)
			.way(Way.ONLINE)
			.platform(Platform.GATHER)
			.platformUrl("platform url")
			.participantCount(1)
			.participantLimit(5)
			.startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
			.endDateTime(LocalDateTime.now().plusMonths(1).truncatedTo(ChronoUnit.MICROS))
			.build();

	public static Study createStudy(StudyStatus studyStatus, String title, Way way, Category category, User user,
									int participantCount, int participantLimit, Platform platform, String platformUrl
	) {
		return Study.builder()
				.status(studyStatus)
				.platform(platform)
				.platformUrl(platformUrl)
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
				platform,
				"www.platformUrl.com"
		);
	}

	public static Study createStudy(User user, String title, int participantLimit,
									StudyStatus studyStatus) {
		return createStudy(
				studyStatus,
				title,
				Way.ONLINE,
				CategoryFixture.createCategory(CategoryFixture.PROJECT),
				user,
				0,
				participantLimit,
				Platform.GATHER,
				"www.platformUrl.com");
	}

	public static Study createStudy(Long id, String title, Way way, Category category, User user,
									Integer participantCount, Integer participantLimit) {
		return Study.builder()
				.id(id)
				.status(StudyStatus.RECRUITING)
				.platform(Platform.GATHER)
				.category(category)
				.owner(user)
				.title(title)
				.way(way)
				.participantCount(participantCount)
				.participantLimit(participantLimit)
				.startDateTime(LocalDateTime.now())
				.endDateTime(LocalDateTime.now().plusDays(5))
				.build();
	}

	public static Study createStudy(Long id, String title, Way way, Category category, User user,
									Integer participantCount, Integer participantLimit,
									LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return Study.builder()
				.id(id)
				.status(StudyStatus.RECRUITING)
				.platform(Platform.GATHER)
				.category(category)
				.owner(user)
				.title(title)
				.way(way)
				.participantCount(participantCount)
				.participantLimit(participantLimit)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

}
