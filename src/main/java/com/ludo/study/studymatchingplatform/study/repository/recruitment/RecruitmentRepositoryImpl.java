package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitmentPosition.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitmentStack.*;

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

}
