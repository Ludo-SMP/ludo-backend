package com.ludo.study.studymatchingplatform.study.repository;

import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;

public interface RecruitmentPositionRepository {
	Set<RecruitmentPosition> saveAll(Recruitment recruitment, Set<Position> positions);
}
