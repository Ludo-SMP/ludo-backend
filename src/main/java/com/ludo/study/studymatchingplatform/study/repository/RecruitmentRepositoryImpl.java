package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl implements RecruitmentRepository {

	private final EntityManager entityManager;

	public Recruitment save(final Recruitment recruitment) {
		entityManager.persist(recruitment);
		return recruitment;
	}

	public Recruitment findById(final Long id) {
		return entityManager.find(Recruitment.class, id);
	}

}
