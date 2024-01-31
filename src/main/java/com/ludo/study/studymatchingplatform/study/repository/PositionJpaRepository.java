package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Position;

public interface PositionJpaRepository extends JpaRepository<Position, Long>, PositionRepository {
}
