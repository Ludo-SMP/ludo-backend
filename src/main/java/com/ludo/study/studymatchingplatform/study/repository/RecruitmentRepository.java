package com.ludo.study.studymatchingplatform.study.repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentRepository {

	Recruitment save(final Recruitment recruitment);

	Recruitment findById(final Long id);

}
