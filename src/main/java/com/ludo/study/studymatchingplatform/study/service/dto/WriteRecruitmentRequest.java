package com.ludo.study.studymatchingplatform.study.service.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import lombok.Getter;

@Getter
public class WriteRecruitmentRequest {
	private Long studyId;
	private String title;
	private List<Long> stackIds;
	private List<Long> positionIds;
	private int recruitmentLimit;
	private LocalDateTime recruitmentEndDateTime;
	private String content;

	public Recruitment toRecruitment(final Study study) {
		return Recruitment.builder()
			.title(title)
			.content(content)
			.recruitmentLimit(recruitmentLimit)
			.recruitmentEndDateTime(recruitmentEndDateTime)
			.study(study)
			.build();
	}

}
