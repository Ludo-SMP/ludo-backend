package com.ludo.study.studymatchingplatform.study.service.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditRecruitmentRequest {

	private final String title;

	private final String callUrl;

	private final Set<Long> stackIds;

	private final Set<Long> positionIds;

	private final Integer recruitmentLimit;

	private final LocalDateTime recruitmentEndDateTime;

	private final String content;

}
