package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QApplicant.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ApplicantRepositoryImpl {

	private final ApplicantJpaRepository applicantJpaRepository;

	private final JPAQueryFactory q;

	public Applicant save(final Applicant applicant) {
		return applicantJpaRepository.save(applicant);
	}

	public Optional<Applicant> find(final Long recruitmentId, final Long userId) {
		return Optional.ofNullable(
				q.selectFrom(applicant)
						.where(applicant.recruitment.id.eq(recruitmentId))
						.where(applicant.user.id.eq(userId))
						.where(applicant.deletedDateTime.isNull())
						.fetchOne()
		);
	}

}
