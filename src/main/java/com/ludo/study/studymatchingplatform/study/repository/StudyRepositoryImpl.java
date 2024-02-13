package com.ludo.study.studymatchingplatform.study.repository;

import static com.ludo.study.studymatchingplatform.study.domain.QStudy.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
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
						.leftJoin(study.recruitment).fetchJoin()
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

	@Transactional
	public Long updateStudyStatus(final Long id, final StudyStatus status) {
		return q.update(study)
				.set(study.status, status)
				.where(study.id.eq(id))
				.execute();
	}

}
