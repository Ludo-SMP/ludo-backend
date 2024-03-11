package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;

public record ApplicantResponse(

		ApplicantStudyResponse study,
		List<ApplicantUserResponse> applicants

) {

	public static ApplicantResponse from(final Study study, final List<Applicant> applicants) {
		final ApplicantStudyResponse studyResponse = ApplicantStudyResponse.from(study);
		final List<ApplicantUserResponse> applicantsResponse = applicants.stream()
				.map(ApplicantUserResponse::from)
				.toList();
		return new ApplicantResponse(studyResponse, applicantsResponse);
	}

}
