package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import java.util.Optional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentRepository {

	Optional<Recruitment> findByIdAndStudyIdentifier(final Long recruitmentId, final Long studyId);

}
