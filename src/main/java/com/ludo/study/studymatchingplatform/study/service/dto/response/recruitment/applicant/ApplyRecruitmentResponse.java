package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplyRecruitmentResponse {

	private final String applicantId;

	// TODO
	public static ApplyRecruitmentResponse from(final Applicant applicant) {
		return null;
	}
}