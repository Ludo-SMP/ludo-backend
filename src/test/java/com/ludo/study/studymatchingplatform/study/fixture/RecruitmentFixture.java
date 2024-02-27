package com.ludo.study.studymatchingplatform.study.fixture;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;

public class RecruitmentFixture {

	public static Recruitment createRecruitment(Study study, String title, String content,
												int hits, String callUrl,
												LocalDateTime endDateTime
	) {
		endDateTime = endDateTime == null ? LocalDateTime.now().plusDays(5) : endDateTime;
		return Recruitment.builder()
				.study(study)
				.callUrl(callUrl)
				.content(content)
				.recruitmentEndDateTime(endDateTime)
				.title(title)
				.hits(hits)
				.build();
	}

	public static Recruitment createRecruitmentWithoutStacksAndPositions(Study study, String title, String content,
																		 String callUrl, LocalDateTime endDateTime) {
		endDateTime = endDateTime == null ? LocalDateTime.now().plusDays(5) : endDateTime;
		return Recruitment.builder()
				.study(study)
				.callUrl(callUrl)
				.content(content)
				.title(title)
				.hits(0)
				.recruitmentEndDateTime(endDateTime)
				.build();
	}

	public static Recruitment createRecruitment(Study study, String title, String content,
												String callUrl,
												List<RecruitmentStack> recruitmentStacks,
												List<RecruitmentPosition> recruitmentPositions
	) {
		return createRecruitment(
				study,
				title,
				content,
				callUrl,
				recruitmentStacks,
				recruitmentPositions
		);
	}
}
