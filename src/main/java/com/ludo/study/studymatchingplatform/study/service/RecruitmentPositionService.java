package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentPositionRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentPositionService {

	private final PositionService positionService;
	private final RecruitmentPositionRepositoryImpl recruitmentPositionRepository;

	public void addMany(final Recruitment recruitment, final Set<Long> positionIds) {
		final List<Position> positions = positionService.findAllByIdsOrThrow(positionIds);

		final List<RecruitmentPosition> recruitmentPositions = positions.stream()
				.map(position -> RecruitmentPosition.from(recruitment, position))
				.toList();

		recruitment.addRecruitmentPositions(recruitmentPositionRepository.saveAll(recruitmentPositions));
	}

	public void update(Recruitment recruitment, Set<Long> positionIds) {
	}
}
