package com.ludo.study.studymatchingplatform.study.service.recruitment.position;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionQueryService {

	private final PositionRepositoryImpl positionRepository;

	public Position readPosition(final Long positionId) {
		return positionRepository.findById(positionId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("id=%d는 존재하지 않는 포지션입니다.", positionId)));
	}

}
