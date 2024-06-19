package com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import java.time.LocalDateTime;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class EditRecruitmentRequest {

	@Schema(description = "수정할 모집 공고 제목", requiredMode = REQUIRED)
	private final String title;

	private final Contact contact;

	@Schema(requiredMode = REQUIRED)
	private final String callUrl;

	@Schema(description = "수정할 스택 id 목록", requiredMode = REQUIRED)
	private final Set<Long> stackIds;

	@Schema(description = "수정할 포지션 id 목록", requiredMode = REQUIRED)
	private final Set<Long> positionIds;

	@Schema(description = "수정할 모집 최대 인원", requiredMode = REQUIRED)
	private final Integer applicantLimit;

	@Schema(description = "수정할 모집 공고 마감 날짜", requiredMode = REQUIRED)
	private final LocalDateTime recruitmentEndDateTime;

	@Schema(description = "수정할 모집 공고 내용", requiredMode = REQUIRED)
	private final String content;

}
