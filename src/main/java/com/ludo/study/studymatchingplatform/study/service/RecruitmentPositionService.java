package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentPositionRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentPositionService {

	private final PositionService positionService;
	private final RecruitmentPositionRepositoryImpl recruitmentPositionRepository;
	private final PositionRepositoryImpl positionRepository;

	public void addMany(final Recruitment recruitment, final Set<Long> positionIds) {
		final List<Position> positions = positionService.findAllByIdsOrThrow(positionIds);
		addRecruitmentPositionsIn(recruitment, positions);
	}

	public void update(final Recruitment recruitment, final Set<Long> positionIds) {

		final Set<Position> nextPositions = positionRepository.findByIdIn(positionIds);
		final List<Position> addedPositions = recruitment.getAddedPositions(nextPositions);

		addRecruitmentPositionsIn(recruitment, addedPositions);
		recruitment.removeRecruitmentPositionsNotIn(nextPositions);
	}

	private void addRecruitmentPositionsIn(final Recruitment recruitment, final List<Position> positions) {
		recruitment.validateDuplicatePositions(positions);

		final List<RecruitmentPosition> recruitmentPositions = positions.stream()
				.map(position -> RecruitmentPosition.from(recruitment, position))
				.toList();

		recruitment.addRecruitmentPositions(recruitmentPositionRepository.saveAll(recruitmentPositions));
	}
}
