package com.ludo.study.studymatchingplatform.study.repository.recruitment.position;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.id.RecruitmentPositionId;

public interface RecruitmentPositionJpaRepository extends JpaRepository<RecruitmentPosition, RecruitmentPositionId> {

}
