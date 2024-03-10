package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant.StudyParticipantResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class StudyDetailsResponse {

	private final long id;

	private final String title;

	private final Way way;

	private final String category;

	private final LocalDateTime startDateTime;

	private final LocalDateTime endDateTime;

	private final int dDay;

	private final int participantsCount;

	private final int participantsLimit;

	private final List<StudyParticipantResponse> participants;

	public static StudyDetailsResponse from(final Study studyDetails) {
		final List<StudyParticipantResponse> participantResponses = studyDetails.getParticipants()
				.stream()
				.map(StudyParticipantResponse::from)
				.toList();

		return new StudyDetailsResponse(studyDetails.getId(), studyDetails.getTitle(), studyDetails.getWay(),
				studyDetails.getCategory().getName(), studyDetails.getStartDateTime(), studyDetails.getEndDateTime(),
				studyDetails.getDday(), studyDetails.getParticipantCount(), studyDetails.getParticipantLimit(),
				participantResponses);
	}

}
