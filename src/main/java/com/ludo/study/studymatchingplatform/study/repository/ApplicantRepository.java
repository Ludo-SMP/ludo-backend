package com.ludo.study.studymatchingplatform.study.repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.User;

public interface ApplicantRepository {
	void create(User user, Recruitment recruitment);

	void updateStatus(Applicant applicant, ApplicantStatus status);

}

