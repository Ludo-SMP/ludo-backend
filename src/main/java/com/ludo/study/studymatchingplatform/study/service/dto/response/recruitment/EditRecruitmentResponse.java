package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.stack.StackResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EditRecruitmentResponse {
	private final EditRecruitment recruitment;

	@Getter
	@RequiredArgsConstructor
	static class EditRecruitment {
		private final Long id;
		private final String ownerNickname;
		private final String title;
		private final String category;
		private final List<StackResponse> stacks;
		private final List<PositionResponse> positions;
		private final Contact contact;
		private final String callUrl;
		private final Integer applicantCount;
		private final LocalDateTime recruitmentEndDateTime;
		private final LocalDateTime startDateTime;
		private final LocalDateTime endDateTime;
		private final String content;
		private final LocalDateTime createdDateTime;
		private final LocalDateTime updatedDateTime;
	}

	public static EditRecruitmentResponse from(final Recruitment recruitment) {
		final List<PositionResponse> positions = recruitment.getPositions().stream()
				.map(PositionResponse::from)
				.toList();
		final List<StackResponse> stacks = recruitment.getStacks().stream()
				.map(StackResponse::from)
				.toList();

		final EditRecruitment editRecruitment = new EditRecruitment(
				recruitment.getId(),
				recruitment.getStudy().getOwnerNickname(),
				recruitment.getTitle(),
				recruitment.getStudy().getCategory().getName(),
				stacks,
				positions,
				recruitment.getContact(),
				recruitment.getCallUrl(),
				recruitment.getApplicantsCount(),
				recruitment.getRecruitmentEndDateTime(),
				recruitment.getStudy().getStartDateTime(),
				recruitment.getStudy().getEndDateTime(),
				recruitment.getContent(),
				recruitment.getCreatedDateTime(),
				recruitment.getUpdatedDateTime()
		);

		return new EditRecruitmentResponse(editRecruitment);
	}

}
