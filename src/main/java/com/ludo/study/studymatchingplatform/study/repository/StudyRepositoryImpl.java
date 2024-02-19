package com.ludo.study.studymatchingplatform.study.repository;

import static com.ludo.study.studymatchingplatform.study.domain.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;
import static com.ludo.study.studymatchingplatform.user.domain.QUser.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl {

	private final JPAQueryFactory q;

	private final StudyJpaRepository studyJpaRepository;

	public Optional<Study> findById(final Long studyId) {
		return studyJpaRepository.findById(studyId);
	}

	public Optional<Study> findByIdWithRecruitment(final Long id) {
		return Optional.ofNullable(
				q.selectFrom(study)
						.where(study.id.eq(id))
						.leftJoin(study.recruitment, recruitment).fetchJoin()
						// .leftJoin(recruitment.recruitmentStacks, recruitmentStack).fetchJoin()
						// .leftJoin(recruitment.recruitmentPositions, recruitmentPosition).fetchJoin()
						.join(study.owner, user).fetchJoin()
						.fetchFirst()
		);
	}

	public boolean hasRecruitment(final Long id) {
		final Long recruitmentId = q.select(study.recruitment.id)
				.from(study)
				.where(study.id.eq(id))
				.fetchOne();

		return recruitmentId != null;
	}

	public Study save(final Study study) {
		return studyJpaRepository.save(study);
	}

}
