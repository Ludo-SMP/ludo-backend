package com.ludo.study.studymatchingplatform.study.service.dto.request;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Platform;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.Builder;

@Builder
public record WriteStudyRequest(

		String title,
		Long categoryId,
		Long positionId,
		Way way,
		Platform platform,
		Integer participantLimit,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime

) {

	public Study toStudy(final User owner, final Category category, final Platform platform) {
		return Study.builder()
				.status(StudyStatus.RECRUITING)
				.title(title)
				.category(category)
				.owner(owner)
				.platform(platform)
				.way(way)
				.participantLimit(participantLimit)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

}
