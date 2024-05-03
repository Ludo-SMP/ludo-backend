package com.ludo.study.studymatchingplatform.study.service.dto.request.study;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record StudyUpdateRequest(

		String title,
		Long categoryId,
		Long positionId,
		String way,
		String platform,
		String platformUrl,
		Integer participantLimit,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime

) {

}
