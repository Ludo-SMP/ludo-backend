package com.ludo.study.studymatchingplatform.study.service.dto.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import java.time.LocalDateTime;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class WriteRecruitmentRequest {

	@Schema(description = "모집 공고 작성할 스터디 id", requiredMode = REQUIRED)
	private final Long studyId;

	@Schema(description = "모집 공고 제목", requiredMode = REQUIRED)
	private final String title;

	@Schema(description = "모집 공고에 등록할 스택 id 목록", requiredMode = REQUIRED)
	private final Set<Long> stackIds;

	@Schema(description = "모집 공고에 등록할 포지션 id 목록", requiredMode = REQUIRED)
	private final Set<Long> positionIds;

	@Schema(description = "모집 공고 최대 인원", requiredMode = REQUIRED)
	private final int recruitmentLimit;

	@Schema(description = "모집 공고 마감 ISOString", requiredMode = REQUIRED)
	private final LocalDateTime recruitmentEndDateTime;

	@Schema(description = "모집 공고 내용", requiredMode = REQUIRED)
	private final String content;

	@Schema(requiredMode = REQUIRED)
	private String callUrl;

	public Recruitment toRecruitment(final Study study) {
		return Recruitment.builder()
				.title(title)
				.content(content)
				.recruitmentLimit(recruitmentLimit)
				.recruitmentEndDateTime(recruitmentEndDateTime)
				.study(study)
				.callUrl(callUrl)
				.build();
	}

}
