package com.ludo.study.studymatchingplatform.user.service.dto.response;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;

import lombok.Builder;

@Builder
public record CompletedStudyResponse(

		Long studyId,
		String title,
		PositionResponse position,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Integer participantCount

) {

	public static CompletedStudyResponse from(final Study study, final Position pos) {
		final PositionResponse positionResponse = PositionResponse.from(pos);
		return CompletedStudyResponse.builder()
				.studyId(study.getId())
				.title(study.getTitle())
				.position(positionResponse)
				.startDateTime(study.getStartDateTime())
				.endDateTime(study.getEndDateTime())
				.participantCount(study.getParticipantCount())
				.build();
	}

}
