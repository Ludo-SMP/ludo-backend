package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentDeleteService {

	private final RecruitmentRepositoryImpl recruitmentRepositoryImpl;
	private final StudyRepositoryImpl studyRepositoryImpl;

	public void deleteRecruitment(final Long studyId) {
		final Recruitment recruitment = findRecruitmentById(studyId);
		recruitmentRepositoryImpl.delete(recruitment);
	}

	private Recruitment findRecruitmentById(final Long id) {
		final Study study = findStudyById(id);
		final Long recruitmentId = study.getRecruitment().getId();
		return recruitmentRepositoryImpl.findById(recruitmentId)
				.orElseThrow(() ->
						new NotFoundException("존재하지 않는 모집 공고 입니다. recruitmentId = " + recruitmentId));
	}

	private Study findStudyById(final Long id) {
		return studyRepositoryImpl.findById(id)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디 입니다. studyId = " + id));
	}

}
