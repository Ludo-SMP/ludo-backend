package com.ludo.study.studymatchingplatform.study.service.dto.request;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Way;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudyCreateRequest(

		Integer userId,

		@NotBlank(message = "스터디의 제목을 입력해주세요.")
		String title,

		@NotNull(message = "스터디의 카테고리를 입력해주세요.")
		Integer categoryId,

		Way way,

		@NotNull(message = "스터디의 최대 참여자를 입력해주세요.")
		Integer participantLimit,

		LocalDateTime startDateTime,

		LocalDateTime endDateTime

) {

}
