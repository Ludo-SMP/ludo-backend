package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, Long> {

}
