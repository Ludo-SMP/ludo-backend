package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl {

	private final JPAQueryFactory queryFactory;
	private final RecruitmentJpaRepository recruitmentJpaRepository;

	public Recruitment save(Recruitment recruitment) {
		recruitmentJpaRepository.save(recruitment);
		return recruitment;
	}

	public Optional<Recruitment> findById(final Long id) {
		return recruitmentJpaRepository.findById(id);
	}

	public List<Recruitment> findRecruitments(final Long recruitmentId, final Integer count) {
		return queryFactory
			.select(recruitment)
			.from(recruitment)
			.where(lessThan(recruitmentId))
			.limit(count)
			.orderBy(recruitment.id.desc())
			.fetch();
	}

	private BooleanExpression lessThan(Long recruitmentId) {
		return recruitmentId != null ? recruitment.id.lt(recruitmentId) : null;
	}

}
