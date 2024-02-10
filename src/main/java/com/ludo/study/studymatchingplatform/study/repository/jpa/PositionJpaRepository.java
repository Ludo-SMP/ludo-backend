package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepository;

public interface PositionJpaRepository extends PositionRepository, JpaRepository<Position, Long> {

	Set<Position> findByIdIn(List<Long> ids);

}
