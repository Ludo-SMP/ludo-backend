package com.ludo.study.studymatchingplatform.study.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.jpa.RecruitmentJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl {

	private final JPAQueryFactory q;

	private final RecruitmentJpaRepository recruitmentJpaRepository;

	public Recruitment save(final Recruitment recruitment) {
		return recruitmentJpaRepository.save(recruitment);
	}

	public Optional<Recruitment> findById(final long id) {
		return recruitmentJpaRepository.findById(id);
	}

	public boolean existsById(final long id) {
		return recruitmentJpaRepository.existsById(id);
	}

}
