package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentDetailsResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecruitmentDetailsFindService {

	private final RecruitmentRepositoryImpl recruitmentRepository;

	@Transactional
	public RecruitmentDetailsResponse findRecruitmentDetails(final Long id) {
		Recruitment recruitment = recruitmentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("지원 공고가 없습니다."));

		recruitment.upHit();
		Study study = recruitment.getStudy();

		return new RecruitmentDetailsResponse(recruitment, study);
	}

}
