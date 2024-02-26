package com.ludo.study.studymatchingplatform.user.service.dto.response;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PositionResponse;

import lombok.Builder;

@Builder
public record CompletedStudyResponse(

		Long studyId,
		String title,
		PositionResponse positionResponse,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Integer participantCount

) {

	public static CompletedStudyResponse from(final Study study, final Position pos) {
		final PositionResponse position = PositionResponse.from(pos);
		return CompletedStudyResponse.builder()
				.studyId(study.getId())
				.title(study.getTitle())
				.positionResponse(position)
				.startDateTime(study.getStartDateTime())
				.endDateTime(study.getEndDateTime())
				.participantCount(study.getParticipantCount())
				.build();
	}

}