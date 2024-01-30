package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentPositionRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentPositionService {

	private final PositionService positionService;
	private final RecruitmentPositionRepositoryImpl recruitmentPositionRepository;

	public List<RecruitmentPosition> createMany(
		final Recruitment recruitment,
		final List<Long> positionIds
	) {
		final List<Position> positions = positionService.findAllByIdsOrThrow(positionIds);

		final List<RecruitmentPosition> recruitmentPositions = positions.stream()
			.filter(position -> !recruitment.hasPosition(positions))
			.map(position -> RecruitmentPosition.from(recruitment, position))
			.toList();

		return recruitmentPositionRepository.saveAll(recruitmentPositions);
	}

}
