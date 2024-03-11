package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;

import lombok.Builder;

@Builder
public record StudyResponse(

		WriteStudyResponse study

) {

	public static StudyResponse from(final Study study) {
		final WriteStudyResponse response = WriteStudyResponse.from(study);

		return StudyResponse.builder()
				.study(response)
				.build();
	}

}
