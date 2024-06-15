package com.ludo.study.studymatchingplatform.study.service.dto.request.study;

import java.time.LocalDateTime;
import java.util.List;

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
		List<Integer> attendanceDay,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime

) {

}
