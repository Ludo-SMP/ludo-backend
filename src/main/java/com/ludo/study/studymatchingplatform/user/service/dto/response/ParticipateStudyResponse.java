package com.ludo.study.studymatchingplatform.user.service.dto.response;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PositionResponse;

import lombok.Builder;

@Builder
public record ParticipateStudyResponse(

		Long studyId,
		String title,
		PositionResponse position,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Integer participantCount

) {

	public static ParticipateStudyResponse from(final Study study, final Position pos) {
		final PositionResponse positionResponse = PositionResponse.from(pos);
		return ParticipateStudyResponse.builder()
				.studyId(study.getId())
				.title(study.getTitle())
				.position(positionResponse)
				.startDateTime(study.getStartDateTime())
				.endDateTime(study.getEndDateTime())
				.participantCount(study.getParticipantCount())
				.build();
	}

}
