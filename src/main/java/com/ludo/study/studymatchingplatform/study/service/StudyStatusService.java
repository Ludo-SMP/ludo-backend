package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.builder.StudyBuilder;
import com.ludo.study.studymatchingplatform.study.service.dto.response.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyStatusService {

	private final StudyRepositoryImpl studyRepository;

	public StudyResponse changeStatus(final Long studyId, final StudyStatus status) {
		final Study study = findById(studyId);
		System.out.println(study);
		study.changeStatus(study, status);
		return StudyBuilder.convertToStudyResponse(study);
	}

	public Study findById(final Long studyId) {
		return studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디 입니다."));
	}

}
