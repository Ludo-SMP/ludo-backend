package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

public record ApplicantStudyResponse(

		Long id,
		UserResponse owner,
		StudyStatus status,
		String title,
		Integer participantLimit,
		Integer participantCount

) {

	public static ApplicantStudyResponse from(final Study study) {
		final UserResponse ownerResponse =
				UserResponse.from(study.getOwner());
		return new ApplicantStudyResponse(
				study.getId(),
				ownerResponse,
				study.getStatus(),
				study.getTitle(),
				study.getParticipantLimit(),
				study.getParticipantCount()
		);
	}

}
