package com.ludo.study.studymatchingplatform.study.service.dto.request.study;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.Builder;

@Builder
public record WriteStudyRequest(

		String title,
		Long categoryId,
		Long positionId,
		String way,
		String platform,
		String platformUrl,
		Integer participantLimit,
		List<Integer> attendanceDay,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime

) {

	public Study toStudy(final User owner, final Category category, final Way way, final Platform platform) {
		return Study.builder()
				.status(StudyStatus.RECRUITING)
				.title(title)
				.category(category)
				.owner(owner)
				.platform(platform)
				.platformUrl(platformUrl)
				.way(way)
				.participantLimit(participantLimit)
				.attendanceDay(attendanceDay)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

}
