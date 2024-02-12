package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionService {

	private final PositionRepositoryImpl positionRepository;

	public List<Position> findAllByIdsOrThrow(final List<Long> positionIds) {
		final List<Position> positions = positionRepository.findAllByIds(positionIds);
		// TODO
		if (positions.size() != positionIds.size()) {
			throw new IllegalArgumentException("존재하지 않는 Position을 입력 하였습니다.");
		}
		return positions;
	}
}
