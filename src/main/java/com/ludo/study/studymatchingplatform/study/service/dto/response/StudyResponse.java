package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;

import lombok.Builder;

@Builder
public record StudyResponse(

		StudyStatus status,
		String title,
		Way way,
		Integer participantLimit,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime

) {

}
