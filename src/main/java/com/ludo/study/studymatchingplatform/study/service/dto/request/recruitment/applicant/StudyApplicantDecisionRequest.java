package com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant;

public record StudyApplicantDecisionRequest(
		Long studyId,
		Long recruitmentId,
		Long applicantUserId
) {
}
