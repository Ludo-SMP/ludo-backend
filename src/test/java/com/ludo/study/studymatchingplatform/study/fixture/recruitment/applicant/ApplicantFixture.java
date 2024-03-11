package com.ludo.study.studymatchingplatform.study.fixture.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public class ApplicantFixture {

	public static Applicant createApplicant(User user, Recruitment recruitment) {
		return Applicant.builder()
				.user(user)
				.applicantStatus(ApplicantStatus.UNCHECKED)
				.recruitment(recruitment)
				.build();
	}

	public static Applicant createApplicant(Recruitment recruitment,
											User user,
											Position position) {
		return Applicant.builder()
				.recruitment(recruitment)
				.user(user)
				.position(position)
				.applicantStatus(ApplicantStatus.UNCHECKED)
				.build();
	}

}
