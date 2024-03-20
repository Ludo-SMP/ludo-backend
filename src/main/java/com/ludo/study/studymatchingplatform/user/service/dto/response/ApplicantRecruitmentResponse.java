package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;

import lombok.Builder;

@Builder
public record ApplicantRecruitmentResponse(

		Long recruitmentId,
		Long studyId,
		String title,
		PositionResponse position,
		ApplicantStatus applicantStatus

) {

	public static ApplicantRecruitmentResponse from(final Applicant applicant) {
		final PositionResponse positionResponse = PositionResponse.from(applicant.getPosition());
		final Recruitment recruitment = applicant.getRecruitment();
		return ApplicantRecruitmentResponse.builder()
				.recruitmentId(recruitment.getId())
				.studyId(recruitment.getStudy().getId())
				.title(recruitment.getTitle())
				.position(positionResponse)
				.applicantStatus(applicant.getApplicantStatus())
				.build();
	}

}
