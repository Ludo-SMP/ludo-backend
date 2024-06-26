package com.ludo.study.studymatchingplatform.study.fixture.recruitment;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RecruitmentFixture {

	public static Recruitment createRecruitment(Study study, String title, String content,
												int hits, String callUrl,
												LocalDateTime endDateTime
	) {
		endDateTime =
				endDateTime == null ? LocalDateTime.now().plusDays(5).truncatedTo(ChronoUnit.MICROS) : endDateTime;
		return Recruitment.builder()
				.study(study)
				.contact(Contact.KAKAO)
				.callUrl(callUrl)
				.content(content)
				.applicantCount(3)
				.recruitmentEndDateTime(endDateTime)
				.title(title)
				.hits(hits)
				.build();
	}

	public static Recruitment createRecruitmentWithoutStacksAndPositions(Study study, String title, String content,
																		 String callUrl, LocalDateTime endDateTime) {
		endDateTime =
				endDateTime == null ? LocalDateTime.now().plusDays(5).truncatedTo(ChronoUnit.MICROS) : endDateTime;
		return Recruitment.builder()
				.study(study)
				.contact(Contact.KAKAO)
				.callUrl(callUrl)
				.applicantCount(3)
				.content(content)
				.title(title)
				.hits(0)
				.recruitmentEndDateTime(endDateTime)
				.modifiedDateTime(LocalDateTime.now())
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

	public static Recruitment createRecruitment(Long id, Study study, String title, String content, Integer hits,
												String callUrl, LocalDateTime endDateTime) {
		return Recruitment.builder()
				.id(id)
				.study(study)
				.contact(Contact.KAKAO)
				.callUrl(callUrl)
				.content(content)
				.applicantCount(3)
				.recruitmentEndDateTime(endDateTime.truncatedTo(ChronoUnit.MICROS))
				.title(title)
				.hits(hits)
				.build();
	}

}
