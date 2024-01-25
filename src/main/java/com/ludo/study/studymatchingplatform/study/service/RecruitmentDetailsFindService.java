package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentRepository;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentDetailsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentDetailsFindService {

	private final RecruitmentRepository recruitmentRepository;

	@Transactional
	public RecruitmentDetailsResponse findRecruitmentDetails(final Long id) {
		Recruitment recruitment = recruitmentRepository.findById(id);
		recruitment.upHit();

		Study study = recruitment.getStudy();
		return new RecruitmentDetailsResponse(recruitment, study);
	}

}
