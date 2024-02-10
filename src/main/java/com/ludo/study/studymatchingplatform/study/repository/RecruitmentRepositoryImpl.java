package com.ludo.study.studymatchingplatform.study.repository;

import static com.ludo.study.studymatchingplatform.study.domain.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitmentPosition.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitmentStack.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl implements RecruitmentRepository {

	private final EntityManager entityManager;
	private final JPAQueryFactory q;

	@Override
	public Optional<Recruitment> findByIdWithDetails(final long id) {
		return Optional.ofNullable(
				q.selectFrom(recruitment)
						.join(recruitment.study, study).fetchJoin()
						.join(recruitment.recruitmentStacks, recruitmentStack).fetchJoin()
						.join(recruitment.recruitmentPositions, recruitmentPosition).fetchJoin()
						.fetchFirst()
		);

	}

	@Override
	public Recruitment save(final Recruitment recruitment) {
		entityManager.persist(recruitment);
		return recruitment;
	}

	@Override
	public Optional<Recruitment> findById(final long id) {
		return Optional.ofNullable(entityManager.find(Recruitment.class, id));
	}

	@Override
	public boolean existsById(final long id) {
		return q.select(recruitment.id)
				.from(recruitment)
				.where(recruitment.id.eq(id))
				.fetchFirst() != null;
	}

	@Override
	public boolean existsByStudyId(final long studyId) {
		return q.select(recruitment.id)
				.from(recruitment)
				.where(recruitment.study.id.eq(studyId))
				.fetchFirst() != null;
	}

}
