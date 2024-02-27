package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;

public class RecruitmentPositionFixture {

	public static RecruitmentPosition createRecruitmentPosition(Position position) {
		return RecruitmentPosition.builder()
				.position(position)
				.build();
	}

	public static RecruitmentPosition createRecruitmentPosition(Recruitment recruitment, Position position) {
		return RecruitmentPosition.builder()
				.recruitment(recruitment)
				.position(position)
				.build();
	}
}
