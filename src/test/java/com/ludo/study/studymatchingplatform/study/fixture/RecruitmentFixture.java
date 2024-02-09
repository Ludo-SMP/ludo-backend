package com.ludo.study.studymatchingplatform.study.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;

public class RecruitmentFixture {

	public static Recruitment createRecruitment(Study study, String title, String content,
			int hits, String callUrl,
			RecruitmentStack... recruitmentStacks
	) {

		return Recruitment.builder()
				.study(study)
				.applicants(new ArrayList<>())
				.callUrl(callUrl)
				.content(content)
				.recruitmentStacks(List.of(recruitmentStacks))
				.recruitmentPositions(new ArrayList<>())
				.recruitmentEndDateTime(LocalDateTime.now().plusDays(5))
				.title(title)
				.hits(hits)
				.build();
	}

}
