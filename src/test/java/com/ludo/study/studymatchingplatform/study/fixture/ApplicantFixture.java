package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.User;

public class ApplicantFixture {

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
