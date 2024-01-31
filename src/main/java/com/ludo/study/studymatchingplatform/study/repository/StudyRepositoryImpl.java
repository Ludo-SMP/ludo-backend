package com.ludo.study.studymatchingplatform.study.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Study;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl {

	private final StudyJpaRepository studyJpaRepository;

	public Optional<Study> findById(final Long id) {
		return studyJpaRepository.findById(id);
	}

}
