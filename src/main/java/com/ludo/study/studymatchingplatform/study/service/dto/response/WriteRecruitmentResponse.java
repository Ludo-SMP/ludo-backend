package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WriteRecruitmentResponse {
	private final long id;
	private final String title;
	// private final StudyResponse study;
	// private final List<ApplicantResponse> applicants;
	private final List<StackResponse> stacks;
	private final List<PositionResponse> positions;
	private final int recruitmentLimit;
	private final LocalDateTime recruitmentEndDateTime;
	private final String callUrl;
	private final String content;

	public static WriteRecruitmentResponse from(final Recruitment recruitment) {
		final List<StackResponse> stacks = recruitment.getRecruitmentStacks().stream()
				.map(recruitmentStack -> StackResponse.from(recruitmentStack.getStack()))
				.toList();
		final List<PositionResponse> positions = recruitment.getRecruitmentPositions().stream()
				.map(recruitmentPosition -> PositionResponse.from(recruitmentPosition.getPosition()))
				.toList();

		return new WriteRecruitmentResponse(
				recruitment.getId(),
				recruitment.getTitle(),
				stacks,
				positions,
				recruitment.getRecruitmentLimit(),
				recruitment.getRecruitmentEndDateTime(),
				recruitment.getCallUrl(),
				recruitment.getContent()
		);
	}
}
