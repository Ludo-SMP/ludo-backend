package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyFetchService {

	private final StudyRepositoryImpl studyRepository;

	public Study getStudyDetails(final User user, final Long studyId) {
		final Study study = studyRepository.findByIdWithParticipants(studyId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디입니다."));

		if (!study.isParticipating(user)) {
			throw new IllegalArgumentException("스터디원만 조회 가능합니다.");
		}
		return study;
	}

}
