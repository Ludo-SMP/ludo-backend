package com.ludo.study.studymatchingplatform.study.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.application.dto.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.persistence.RecruitmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentFindService {

	private final RecruitmentRepository recruitmentRepository;

	@Transactional
	public RecruitmentDetailsResponse findRecruitmentById(final Long id) {
		Recruitment recruitment = recruitmentRepository.findById(id);
		Study study = recruitment.getStudy();
		return new RecruitmentDetailsResponse(recruitment, study);
	}

}
