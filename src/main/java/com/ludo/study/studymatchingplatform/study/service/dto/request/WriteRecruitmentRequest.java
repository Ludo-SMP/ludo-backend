package com.ludo.study.studymatchingplatform.study.service.dto.request;

import java.time.LocalDateTime;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class WriteRecruitmentRequest {
	private final Long studyId;
	private final String title;
	private final Set<Long> stackIds;
	private final Set<Long> positionIds;
	private final int recruitmentLimit;
	private final LocalDateTime recruitmentEndDateTime;
	private final String content;
	private final String callUrl;

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
