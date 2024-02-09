package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryImpl {

	private final PositionJpaRepository positionJpaRepository;

}
