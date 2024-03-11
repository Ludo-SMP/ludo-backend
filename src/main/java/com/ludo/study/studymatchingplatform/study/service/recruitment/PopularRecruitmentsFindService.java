package com.ludo.study.studymatchingplatform.study.service.recruitment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.PopularRecruitmentCond;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.mapper.RecruitmentPreviewResponseMapper;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.PopularRecruitmentsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PopularRecruitmentsFindService {

	private final RecruitmentRepositoryImpl recruitmentRepository;
	private final RecruitmentPreviewResponseMapper recruitmentPreviewResponseMapper;

	@Transactional
	public PopularRecruitmentsResponse findPopularRecruitments(final PopularRecruitmentCond cond) {

		List<Recruitment> popularProject = findPopularProjects(cond);
		List<Recruitment> popularCoding = findPopularCodingTests(cond);
		List<Recruitment> popularInterview = findPopularInterviews(cond);

		return new PopularRecruitmentsResponse(
				recruitmentPreviewResponseMapper.mapBy(popularCoding),
				recruitmentPreviewResponseMapper.mapBy(popularInterview),
				recruitmentPreviewResponseMapper.mapBy(popularProject)
		);
	}

	private List<Recruitment> findPopularProjects(final PopularRecruitmentCond cond) {
		return recruitmentRepository.findPopularRecruitments(1L, cond);
	}

	private List<Recruitment> findPopularCodingTests(final PopularRecruitmentCond cond) {
		return recruitmentRepository.findPopularRecruitments(2L, cond);
	}

	private List<Recruitment> findPopularInterviews(final PopularRecruitmentCond cond) {
		return recruitmentRepository.findPopularRecruitments(3L, cond);
	}

}
