package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;

public record ApplyRecruitmentResponse(String applicantId) {
	public static ApplyRecruitmentResponse from(final Applicant applicant) {
		return new ApplyRecruitmentResponse(applicant.getId().toString());
	}
}
