package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
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

	public Optional<Recruitment> findByIdWithStudy(final long id) {
		return Optional.ofNullable(
				q.selectFrom(recruitment)
						.leftJoin(recruitment.study).fetchJoin()
						.where(recruitment.id.eq(id))
						.fetchOne()
		);
	}
}
