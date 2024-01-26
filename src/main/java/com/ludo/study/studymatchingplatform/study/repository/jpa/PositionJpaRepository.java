package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepository;

public interface PositionJpaRepository extends PositionRepository, JpaRepository<Position, Long> {
}
