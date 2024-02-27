package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.Platform;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

import lombok.Builder;

@Builder
public record WriteStudyResponse(

		Long id,
		StudyStatus status,
		CategoryResponse category,
		UserResponse owner,
		String title,
		Platform platform,
		List<ParticipantResponse> participants,
		Way way,
		Integer participantLimit,
		Integer participantCount,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime

) {

	public static WriteStudyResponse from(final Study study) {
		final List<ParticipantResponse> participants = study.getParticipants().stream()
				.map(participant -> ParticipantResponse.from(participant))
				.toList();

		final CategoryResponse category = CategoryResponse.from(study.getCategory());
		final UserResponse owner = UserResponse.from(study.getOwner());

		return WriteStudyResponse.builder()
				.id(study.getId())
				.status(study.getStatus())
				.category(category)
				.owner(owner)
				.title(study.getTitle())
				.platform(study.getPlatform())
				.participants(participants)
				.way(study.getWay())
				.participantLimit(study.getParticipantLimit())
				.participantCount(study.getParticipantCount())
				.startDateTime(study.getStartDateTime())
				.endDateTime(study.getEndDateTime())
				.build();
	}

}
