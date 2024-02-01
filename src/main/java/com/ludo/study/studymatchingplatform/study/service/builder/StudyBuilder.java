package com.ludo.study.studymatchingplatform.study.service.builder;

import com.ludo.study.studymatchingplatform.study.service.dto.StudyCreateDto;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyCreateRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StudyBuilder {

	public static StudyCreateDto convertToStudyCreateDto(final StudyCreateRequest request) {
		return StudyCreateDto.builder()
				.title(request.title())
				.way(request.way())
				.participantLimit(request.participantLimit())
				.startDateTime(request.startDateTime())
				.endDateTime(request.endDateTime())
				.build();
	}

}
