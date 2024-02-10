package com.ludo.study.studymatchingplatform.study.repository;

import java.util.Optional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentRepository {

	Optional<Recruitment> findById(long id);

	Optional<Recruitment> findByIdWithDetails(long id);

	Recruitment save(Recruitment recruitment);

	boolean existsById(long id);

	boolean existsByStudyId(long studyId);
	//
	// Optional<Recruitment> findById(final Long id);
	//
	// void deleteById(Long id);
	//
	// List<Recruitment> findAll();

}
