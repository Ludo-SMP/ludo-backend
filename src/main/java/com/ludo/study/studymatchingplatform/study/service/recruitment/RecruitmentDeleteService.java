package com.ludo.study.studymatchingplatform.study.service.recruitment;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentDeleteService {

	private final RecruitmentRepositoryImpl recruitmentRepository;
	private final StudyRepositoryImpl studyRepository;

	public void deleteRecruitment(final Long studyId) {
		final Recruitment recruitment = findRecruitmentById(studyId);
		recruitmentRepository.delete(recruitment);
	}

	private Recruitment findRecruitmentById(final Long id) {
		final Study study = findStudyById(id);
		final Long recruitmentId = study.getRecruitment().getId();
		return recruitmentRepository.findById(recruitmentId)
				.orElseThrow(() ->
						new NotFoundException("존재하지 않는 모집 공고 입니다. recruitmentId = " + recruitmentId));
	}

	private Study findStudyById(final Long id) {
		return studyRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디 입니다. studyId = " + id));
	}

}
