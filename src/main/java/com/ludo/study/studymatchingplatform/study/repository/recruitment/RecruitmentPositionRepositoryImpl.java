package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentPositionRepositoryImpl {

	private final RecruitmentPositionJpaRepository recruitmentPositionJpaRepository;

}
