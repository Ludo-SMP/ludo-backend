package com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.id.ApplicantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, ApplicantId> {

}
