package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.WriteStudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyStatusService {

	private final StudyRepositoryImpl studyRepository;

	public WriteStudyResponse changeStatus(final Long studyId, final StudyStatus status) {
		final Long updatedId = studyRepository.updateStudyStatus(studyId, status);
		final Study study = findById(updatedId);
		return WriteStudyResponse.from(study);
	}

	public Study findById(final Long studyId) {
		return studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디 입니다."));
	}

}
