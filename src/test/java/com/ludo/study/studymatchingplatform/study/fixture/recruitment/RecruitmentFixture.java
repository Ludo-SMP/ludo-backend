package com.ludo.study.studymatchingplatform.study.fixture.recruitment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

public class RecruitmentFixture {

	/**
	 * 해당 Fixture을 사용하기 위해 연관 관계 설정이 필요합니다.
	 * 1. study - recruitment
	 * 2. recruitment - List<recruitmentStack>
	 * 3. recruitment - List<recruitmentPosition>
	 * 4(Optional). recruitment - List<applicant>
	 */
	public static final Recruitment recruitment1 = Recruitment.builder()
			.contact(Contact.KAKAO)
			.callUrl("callUrl")
			.title("모집공고1 제목")
			.content("모집공고1 내용")
			.applicantCount(5)
			.recruitmentEndDateTime(LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MICROS))
			.hits(1)
			.build();

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
