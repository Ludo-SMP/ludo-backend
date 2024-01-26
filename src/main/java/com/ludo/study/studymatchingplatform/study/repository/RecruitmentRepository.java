package com.ludo.study.studymatchingplatform.study.repository;

import java.util.List;
import java.util.Optional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentRepository {
	Optional<Recruitment> findById(long id);

	Recruitment save(Recruitment recruitment);

	Optional<Recruitment> findById(final Long id);

	void deleteById(Long id);

	List<Recruitment> findAll();
}
