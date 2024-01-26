package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.repository.ApplicantRepository;

public interface ApplicantJpaRepository extends ApplicantRepository, JpaRepository<Applicant, Long> {
}
