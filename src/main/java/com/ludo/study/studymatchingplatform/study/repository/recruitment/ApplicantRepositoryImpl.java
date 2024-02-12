package com.ludo.study.studymatchingplatform.study.repository.recruitment;

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
}
