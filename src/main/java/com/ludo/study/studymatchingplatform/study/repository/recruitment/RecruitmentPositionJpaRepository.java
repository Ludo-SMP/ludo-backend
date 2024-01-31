package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;

public interface RecruitmentPositionJpaRepository extends JpaRepository<RecruitmentPosition, Long>,
		RecruitmentPositionRepository {

}
