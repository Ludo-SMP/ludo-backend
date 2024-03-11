package com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import java.time.LocalDateTime;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class WriteRecruitmentRequest {

	@Schema(description = "모집 공고 제목", requiredMode = REQUIRED)
	private final String title;

	@Schema(description = "모집 공고에 등록할 스택 id 목록", requiredMode = REQUIRED)
	private final Set<Long> stackIds;

	@Schema(description = "모집 공고에 등록할 포지션 id 목록", requiredMode = REQUIRED)
	private final Set<Long> positionIds;

	@Schema(description = "모집 공고 최대 인원", requiredMode = REQUIRED)
	private final Integer applicantCount;

	@Schema(description = "모집 공고 마감 ISOString", requiredMode = REQUIRED)
	private final LocalDateTime recruitmentEndDateTime;

	@Schema(description = "모집 공고 내용", requiredMode = REQUIRED)
	private final String content;

	private final Contact contact;

	@Schema(requiredMode = REQUIRED)
	private final String callUrl;

	public Recruitment toRecruitment(final Study study) {
		return Recruitment.builder()
				.title(title)
				.content(content)
				.applicantCount(applicantCount)
				.recruitmentEndDateTime(recruitmentEndDateTime)
				.study(study)
				.contact(contact)
				.callUrl(callUrl)
				.build();
	}

}
