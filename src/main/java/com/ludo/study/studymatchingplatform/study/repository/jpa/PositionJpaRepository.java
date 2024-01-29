package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Position;

public interface PositionJpaRepository extends JpaRepository<Position, Long> {
	Set<Position> findByIdIn(List<Long> positionIds);

}
