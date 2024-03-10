package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.stack.StackResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WriteRecruitmentResponse {

	private final WriteRecruitment recruitment;

	@Getter
	@RequiredArgsConstructor
	static class WriteRecruitment {
		private final long id;
		private final String ownerNickname;
		private final String title;
		private final String category;
		private final List<StackResponse> stacks;
		private final List<PositionResponse> positions;
		private final Platform platform;
		private final int applicantCount;
		private final LocalDateTime recruitmentEndDateTime;
		private final LocalDateTime startDateTime;
		private final LocalDateTime endDateTime;
		private final LocalDateTime createdDateTime;
		private final Contact contect;
		private final String callUrl;
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
				recruitment.getStudy().getPlatform(), // TODO
				recruitment.getApplicantsCount(),
				recruitment.getRecruitmentEndDateTime(),
				recruitment.getStudy().getStartDateTime(),
				recruitment.getStudy().getEndDateTime(),
				recruitment.getCreatedDateTime(),
				recruitment.getContect(),
				recruitment.getCallUrl(),
				recruitment.getContent()
		));
	}
}
