package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.stack.StackResponse;

public record EditRecruitmentResponse(
		Long id, String ownerNickname, String title, String category, List<StackResponse> stacks,
		List<PositionResponse> positions, Contact contact, String callUrl, Integer applicantCount,
		LocalDateTime recruitmentEndDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime,
		String content, LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {

	public static EditRecruitmentResponse from(final Recruitment recruitment) {
		final List<PositionResponse> positions = recruitment.getPositions().stream()
				.map(PositionResponse::from)
				.toList();
		final List<StackResponse> stacks = recruitment.getStacks().stream()
				.map(StackResponse::from)
				.toList();

		return new EditRecruitmentResponse(
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

	}

}