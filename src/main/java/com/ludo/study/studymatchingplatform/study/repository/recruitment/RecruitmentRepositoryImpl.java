package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.QCategory.*;
import static com.ludo.study.studymatchingplatform.study.domain.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl {

	private final JPAQueryFactory jpaQueryFactory;
	private final RecruitmentJpaRepository recruitmentJpaRepository;

	public Recruitment save(Recruitment recruitment) {
		recruitmentJpaRepository.save(recruitment);
		return recruitment;
	}

	public Optional<Recruitment> findById(final Long id) {
		return recruitmentJpaRepository.findById(id);
	}

	public List<Recruitment> findPopularRecruitments(final String categoryName) {
		return jpaQueryFactory
			.select(recruitment)
			.from(recruitment)
			.join(recruitment.study, study)
			.join(study.category, category)
			.where(category.name.eq(categoryName))
			.limit(3)
			.orderBy(recruitment.hits.desc())
			.fetch();
	}

}
