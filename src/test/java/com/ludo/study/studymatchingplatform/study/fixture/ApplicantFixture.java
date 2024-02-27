package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class ApplicantFixture {

	public static Applicant createApplicant(User user, Recruitment recruitment) {
		return Applicant.builder()
				.user(user)
				.applicantStatus(ApplicantStatus.UNCHECKED)
				.recruitment(recruitment)
				.build();
	}
}
