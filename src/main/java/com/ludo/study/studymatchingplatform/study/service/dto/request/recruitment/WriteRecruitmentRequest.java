package com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment;

import java.time.LocalDateTime;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class WriteRecruitmentRequest {
	private final Long studyId;
	private final String title;
	private final Set<Long> stackIds;
	private final Set<Long> positionIds;
	private final Integer applicantCount;
	private final LocalDateTime recruitmentEndDateTime;
	private final String content;
	private final Contact contect;
	private final String callUrl;

	public Recruitment toRecruitment(final Study study, final Contact contect) {
		return Recruitment.builder()
				.title(title)
				.content(content)
				.applicantCount(applicantCount)
				.recruitmentEndDateTime(recruitmentEndDateTime)
				.study(study)
				.contect(contect)
				.callUrl(callUrl)
				.build();
	}

}
