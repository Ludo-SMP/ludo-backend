package com.ludo.study.studymatchingplatform.study.repository.recruitment.position;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.id.RecruitmentPositionId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;

public interface RecruitmentPositionJpaRepository extends JpaRepository<RecruitmentPosition, RecruitmentPositionId> {

}
