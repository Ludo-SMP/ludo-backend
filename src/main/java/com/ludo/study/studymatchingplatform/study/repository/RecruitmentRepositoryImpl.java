package com.ludo.study.studymatchingplatform.study.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl implements RecruitmentRepository {

	private final EntityManager entityManager;

	public Recruitment save(final Recruitment recruitment) {
		entityManager.persist(recruitment);
		return recruitment;
	}

	public Optional<Recruitment> findById(final Long id) {
		return Optional.ofNullable(entityManager.find(Recruitment.class, id));
	}

}
