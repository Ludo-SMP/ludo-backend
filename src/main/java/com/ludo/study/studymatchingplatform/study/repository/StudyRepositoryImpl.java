package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Study;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl {

	private final StudyJpaRepository studyJpaRepository;

	public Study save(final Study study) {
		studyJpaRepository.save(study);
		return study;
	}

}
