package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.QCategory.*;
import static com.ludo.study.studymatchingplatform.study.domain.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitmentPosition.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitmentStack.*;

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

	private final JPAQueryFactory q;
	private final RecruitmentJpaRepository recruitmentJpaRepository;

	public Optional<Recruitment> findById(final Long recruitmentId) {
		return recruitmentJpaRepository.findById(recruitmentId);
	}

	public Optional<Recruitment> findByIdWithStudy(final Long id) {
		return Optional.ofNullable(
				q.selectFrom(recruitment)
						.join(recruitment.study, study).fetchJoin()
						// .innerJoin(recruitment.applicants, applicant).fetchJoin()
						.where(recruitment.id.eq(id))
						.fetchOne()
		);
	}

	public Optional<Recruitment> findByIdWithDetails(final Long id) {
		return Optional.ofNullable(
				q.selectFrom(recruitment)
						.join(recruitment.study, study).fetchJoin()
						.join(recruitment.recruitmentStacks, recruitmentStack).fetchJoin()
						.join(recruitment.recruitmentPositions, recruitmentPosition).fetchJoin()
						.fetchFirst()
		);
	}

	public boolean existsByStudyId(final Long studyId) {
		return q.select(recruitment.id)
				.from(recruitment)
				.where(recruitment.study.id.eq(studyId))
				.fetchFirst() != null;
	}

	public boolean existsById(final Long id) {
		return recruitmentJpaRepository.existsById(id);
	}

	public Recruitment save(final Recruitment recruitment) {
		return recruitmentJpaRepository.save(recruitment);
	}

	public void delete(final Recruitment recruitment) {
		recruitmentJpaRepository.delete(recruitment);
	}

	public List<Recruitment> findPopularRecruitments(final String categoryName) {
		return q.select(recruitment)
				.from(recruitment)
				.join(recruitment.study, study)
				.join(study.category, category)
				.where(category.name.eq(categoryName))
				.limit(3)
				.orderBy(recruitment.hits.desc())
				.fetch();
	}

	public List<Recruitment> findRecruitments(final Long recruitmentId, final Integer count) {
		return q
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
