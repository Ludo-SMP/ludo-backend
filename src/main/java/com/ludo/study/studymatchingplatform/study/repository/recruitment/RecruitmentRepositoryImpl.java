package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;

import java.util.Optional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.QuerydslRepositorySupporter;
import com.querydsl.core.types.dsl.BooleanExpression;

public class RecruitmentRepositoryImpl extends QuerydslRepositorySupporter implements RecruitmentRepository {

	public RecruitmentRepositoryImpl() {
		super(Recruitment.class);
	}

	@Override
	public Optional<Recruitment> findByIdAndStudyIdentifier(final Long recruitmentId, final Long studyId) {
		return Optional.ofNullable(selectFrom(recruitment)
				.where(creatorIdentifierCond(studyId),
						recruitmentCond(recruitmentId))
				.fetchOne());
	}

	private BooleanExpression creatorIdentifierCond(final Long studyId) {
		return recruitment.study.id.eq(studyId);
	}

	private BooleanExpression recruitmentCond(final Long recruitmentId) {
		return recruitment.id.eq(recruitmentId);
	}

}
