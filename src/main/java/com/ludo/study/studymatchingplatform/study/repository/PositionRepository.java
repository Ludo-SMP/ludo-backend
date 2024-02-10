package com.ludo.study.studymatchingplatform.study.repository;

import java.util.List;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.Position;

public interface PositionRepository {
	Set<Position> findByIdIn(List<Long> ids);

	// Set<Position> findAllByNameIn(List<String> positionNames);
	//
	// Set<Position> findManyByIdIn(Set<Long> ids);

}
