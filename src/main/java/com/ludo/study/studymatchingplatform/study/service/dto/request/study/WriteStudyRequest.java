package com.ludo.study.studymatchingplatform.study.service.dto.request.study;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

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
