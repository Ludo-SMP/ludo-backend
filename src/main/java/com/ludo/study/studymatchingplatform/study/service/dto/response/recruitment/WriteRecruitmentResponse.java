package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.stack.StackResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WriteRecruitmentResponse {

	private final WriteRecruitment recruitment;

	@Getter
	@RequiredArgsConstructor
	@Schema(description = "작성된 모집 공고")
	static class WriteRecruitment {

		@Schema(description = "모집 공고 id")
		private final Long id;

		@Schema(description = "모집 공고 작성자명")
		private final String ownerNickname;

		@Schema(description = "모집 공고 제목")
		private final String title;

		@Schema(description = "스터디 카테고리")
		private final String category;

		@Schema(description = "사용 스택 목록")
		private final List<StackResponse> stacks;

		@Schema(description = "모집 포지션 목록")
		private final List<PositionResponse> positions;

		@Schema(description = "사용할 미팅 플랫폼")
		private final Platform platform;

		@Schema(description = "지원자 수")
		private final Integer applicantCount;

		@Schema(description = "모집 마감 날짜")
		private final LocalDateTime recruitmentEndDateTime;

		@Schema(description = "스터디 시작 날짜")
		private final LocalDateTime startDateTime;

		@Schema(description = "스터디 종료 날짜")
		private final LocalDateTime endDateTime;

		@Schema(description = "모집 공고 생성 날짜")
		private final LocalDateTime createdDateTime;

		@Schema(description = "모집 공고 연락 방법")
		private final Contact contact;

		@Schema(description = "모집 공고 연락 URL")
		private final String callUrl;

		@Schema(description = "모집 공고 내용")
		private final String content;

	}

	public static WriteRecruitmentResponse from(final Recruitment recruitment) {
		final List<StackResponse> stacks = recruitment.getRecruitmentStacks().stream()
				.map(recruitmentStack -> StackResponse.from(recruitmentStack.getStack()))
				.toList();
		final List<PositionResponse> positions = recruitment.getRecruitmentPositions().stream()
				.map(recruitmentPosition -> PositionResponse.from(recruitmentPosition.getPosition()))
				.toList();

		return new WriteRecruitmentResponse(new WriteRecruitment(
				recruitment.getId(),
				recruitment.getStudy().getOwnerNickname(),
				recruitment.getTitle(),
				recruitment.getStudy().getCategory().getName(),
				stacks,
				positions,
				recruitment.getStudy().getPlatform(),
				recruitment.getApplicantsCount(),
				recruitment.getRecruitmentEndDateTime(),
				recruitment.getStudy().getStartDateTime(),
				recruitment.getStudy().getEndDateTime(),
				recruitment.getCreatedDateTime(),
				recruitment.getContact(),
				recruitment.getCallUrl(),
				recruitment.getContent()
		));
	}
}
