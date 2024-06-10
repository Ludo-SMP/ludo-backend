package com.ludo.study.studymatchingplatform.study.fixture.study;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.service.study.FixedUtcDateTimePicker;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public class StudyFixture {

	public static Study PROJECT_ONLINE_STUDY(User owner, UtcDateTimePicker utcDateTimePicker) {

		return Study.builder()
				.title("프로젝트 온라인 스터디 제목")
				.owner(owner)
				.category(CategoryFixture.CATEGORY_PROJECT)
				.status(StudyStatus.RECRUITING)
				.way(Way.ONLINE)
				.platform(Platform.GATHER)
				.platformUrl("platform url")
				.participantCount(1)
				.participantLimit(5)
				.startDateTime(utcDateTimePicker.now().truncatedTo(ChronoUnit.MICROS))
				.endDateTime(utcDateTimePicker.now().plusMonths(1).truncatedTo(ChronoUnit.MICROS))
				.build();
	}

	public static Study STUDY1(User owner, LocalDateTime startDateTime, LocalDateTime endDateTime) {

		return Study.builder()
				.title("스터디1 제목")
				.owner(owner)
				.category(CategoryFixture.CATEGORY_PROJECT)
				.status(StudyStatus.RECRUITING)
				.way(Way.ONLINE)
				.platform(Platform.GATHER)
				.platformUrl("platform url")
				.participantCount(1)
				.participantLimit(5)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime.truncatedTo(ChronoUnit.MICROS))
				.build();
	}

	public static Study STUDY2(User owner, LocalDateTime startDateTime, LocalDateTime endDateTime) {

		return Study.builder()
				.title("스터디2 제목")
				.owner(owner)
				.category(CategoryFixture.CATEGORY_PROJECT)
				.status(StudyStatus.RECRUITING)
				.way(Way.ONLINE)
				.platform(Platform.GATHER)
				.platformUrl("platform url")
				.participantCount(1)
				.participantLimit(5)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime.truncatedTo(ChronoUnit.MICROS))
				.build();
	}

	public static Study STUDY3(User owner, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();

		return Study.builder()
				.title("스터디3 제목")
				.owner(owner)
				.category(CategoryFixture.CATEGORY_PROJECT)
				.status(StudyStatus.RECRUITING)
				.way(Way.ONLINE)
				.platform(Platform.GATHER)
				.platformUrl("platform url")
				.participantCount(1)
				.participantLimit(5)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime.truncatedTo(ChronoUnit.MICROS))
				.build();
	}

	public static Study STUDY4(User owner, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();

		return Study.builder()
				.title("스터디4 제목")
				.owner(owner)
				.category(CategoryFixture.CATEGORY_PROJECT)
				.status(StudyStatus.RECRUITING)
				.way(Way.ONLINE)
				.platform(Platform.GATHER)
				.platformUrl("platform url")
				.participantCount(1)
				.participantLimit(5)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime.truncatedTo(ChronoUnit.MICROS))
				.build();
	}

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
				CategoryFixture.createCategory(CategoryFixture.PROJECT_NAME),
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
