package com.ludo.study.studymatchingplatform.study.repository;

import java.util.Collection;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;

public interface RecruitmentPositionRepository {
	Set<RecruitmentPosition> saveAll(Collection<RecruitmentPosition> recruitmentPositions);

	// Set<RecruitmentPosition> saveAll(Recruitment recruitment, Set<Position> positions);

}
