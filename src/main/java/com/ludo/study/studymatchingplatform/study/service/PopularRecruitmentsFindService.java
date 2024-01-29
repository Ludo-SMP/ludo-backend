package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentRepository;
import com.ludo.study.studymatchingplatform.study.service.dto.mapper.RecruitmentPreviewResponseMapper;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PopularRecruitmentsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PopularRecruitmentsFindService {

	private final RecruitmentRepository recruitmentRepository;
	private final RecruitmentPreviewResponseMapper recruitmentPreviewResponseMapper;

	@Transactional
	public PopularRecruitmentsResponse findPopularRecruitments() {
		PopularRecruitmentCondition popularRecruitmentCondition = new PopularRecruitmentCondition();

		List<Recruitment> popularCoding = recruitmentRepository.findPopularRecruitments("코딩테스트",
			popularRecruitmentCondition.getCondition());
		List<Recruitment> popularInterview = recruitmentRepository.findPopularRecruitments("모의면접",
			popularRecruitmentCondition.getCondition());
		List<Recruitment> popularProject = recruitmentRepository.findPopularRecruitments("프로젝트",
			popularRecruitmentCondition.getCondition());

		return new PopularRecruitmentsResponse(
			recruitmentPreviewResponseMapper.mapBy(popularCoding),
			recruitmentPreviewResponseMapper.mapBy(popularInterview),
			recruitmentPreviewResponseMapper.mapBy(popularProject)
		);
	}

}
