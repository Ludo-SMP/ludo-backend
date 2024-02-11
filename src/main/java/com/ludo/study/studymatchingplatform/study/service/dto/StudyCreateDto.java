package com.ludo.study.studymatchingplatform.study.service.dto;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Way;

import lombok.Builder;

@Builder
public record StudyCreateDto(

		String title,
		Way way,
		Integer participantLimit,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime

) {

}
