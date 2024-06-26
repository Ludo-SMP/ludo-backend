package com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.QApplicant.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.id.ApplicantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
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
						.fetchOne());
	}

	public List<Applicant> findMyPageApplyRecruitmentInfoByUserId(final Long id) {
		return q.selectFrom(applicant)
				.where(applicant.user.id.eq(id))
				.where(applicant.deletedDateTime.isNull())
				.fetch();
	}

	public List<Applicant> findStudyApplicantInfoByRecruitmentId(final Long id) {
		return q.selectFrom(applicant)
				.where(applicant.recruitment.id.eq(id))
				.where(applicant.applicantStatus.ne(ApplicantStatus.REFUSED))
				.where(applicant.deletedDateTime.isNull())
				.fetch();
	}

	public Optional<Applicant> findById(final ApplicantId applicantId) {
		return Optional.ofNullable(
				q.selectFrom(applicant)
						.where(applicant.id.eq(applicantId))
						.where(applicant.deletedDateTime.isNull())
						.fetchOne());
	}

}
