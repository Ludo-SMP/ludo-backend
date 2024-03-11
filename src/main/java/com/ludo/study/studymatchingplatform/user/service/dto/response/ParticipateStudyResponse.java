package com.ludo.study.studymatchingplatform.user.service.dto.response;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.Builder;

@Builder
public record ParticipateStudyResponse(

		Long studyId,
		String title,
		PositionResponse position,
		StudyStatus status,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Integer participantCount,
		Boolean isOwner,
		Boolean hasRecruitment

) {

	public static ParticipateStudyResponse from(final User user, final Study study, final Position pos) {
		final PositionResponse positionResponse = PositionResponse.from(pos);
		return ParticipateStudyResponse.builder()
				.studyId(study.getId())
				.title(study.getTitle())
				.position(positionResponse)
				.status(study.getStatus())
				.startDateTime(study.getStartDateTime())
				.endDateTime(study.getEndDateTime())
				.participantCount(study.getParticipantCount())
				.isOwner(study.isOwner(user))
				.hasRecruitment(study.ensureHasRecruitment())
				.build();
	}

}
