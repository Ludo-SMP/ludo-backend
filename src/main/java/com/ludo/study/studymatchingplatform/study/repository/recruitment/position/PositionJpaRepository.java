package com.ludo.study.studymatchingplatform.study.repository.recruitment.position;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;

public interface PositionJpaRepository extends JpaRepository<Position, Long> {
	Set<Position> findByIdIn(Set<Long> positionIds);

}
