package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.PopularRecruitmentCond;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.mapper.RecruitmentPreviewResponseMapper;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PopularRecruitmentsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PopularRecruitmentsFindService {

	private final RecruitmentRepositoryImpl recruitmentRepository;
	private final RecruitmentPreviewResponseMapper recruitmentPreviewResponseMapper;

	@Transactional
	public PopularRecruitmentsResponse findPopularRecruitments(final PopularRecruitmentCond cond) {

		List<Recruitment> popularCoding = recruitmentRepository.findPopularRecruitments("코딩 테스트", cond);
		List<Recruitment> popularInterview = recruitmentRepository.findPopularRecruitments("모의 면접", cond);
		List<Recruitment> popularProject = recruitmentRepository.findPopularRecruitments("프로젝트", cond);

		return new PopularRecruitmentsResponse(
				recruitmentPreviewResponseMapper.mapBy(popularCoding),
				recruitmentPreviewResponseMapper.mapBy(popularInterview),
				recruitmentPreviewResponseMapper.mapBy(popularProject)
		);
	}

}
