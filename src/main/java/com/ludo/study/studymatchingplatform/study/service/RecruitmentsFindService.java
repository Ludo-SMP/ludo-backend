package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCursor;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.mapper.RecruitmentPreviewResponseMapper;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentsFindService {

	private final RecruitmentRepositoryImpl recruitmentRepository;
	private final RecruitmentPreviewResponseMapper recruitmentPreviewResponseMapper;

	@Transactional
	public List<RecruitmentPreviewResponse> findRecruitments(final RecruitmentFindCursor recruitmentFindCursor,
															 final RecruitmentFindCond recruitmentFindCond) {
		
		List<Recruitment> recruitments = recruitmentRepository.findRecruitments(recruitmentFindCursor,
				recruitmentFindCond);

		return recruitmentPreviewResponseMapper.mapBy(recruitments);
	}
}
