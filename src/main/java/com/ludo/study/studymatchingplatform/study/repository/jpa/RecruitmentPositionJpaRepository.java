package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentPositionRepository;

public interface RecruitmentPositionJpaRepository extends
	RecruitmentPositionRepository, JpaRepository<RecruitmentPosition, Long> {
}
