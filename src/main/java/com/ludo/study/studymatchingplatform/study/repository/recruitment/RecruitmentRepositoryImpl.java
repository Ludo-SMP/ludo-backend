package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.position.QRecruitmentPosition.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.QRecruitmentStack.*;
import static com.ludo.study.studymatchingplatform.study.domain.study.QStudy.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.PopularRecruitmentCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCursor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl {

	private final JPAQueryFactory q;
	private final RecruitmentJpaRepository recruitmentJpaRepository;

	public Optional<Recruitment> findById(final Long recruitmentId) {
		return recruitmentJpaRepository.findById(recruitmentId);
	}

	public Optional<Recruitment> findByIdWithStudy(final Long id) {
		return Optional.ofNullable(
				q.selectFrom(recruitment)
						.join(recruitment.study, study).fetchJoin()
						// .innerJoin(recruitment.applicants, applicant).fetchJoin()
						.where(recruitment.id.eq(id))
						.fetchOne()
		);
	}

	public Optional<Recruitment> findByIdWithDetails(final Long id) {
		return Optional.ofNullable(
				q.selectFrom(recruitment)
						.join(recruitment.study, study).fetchJoin()
						.join(recruitment.recruitmentStacks, recruitmentStack).fetchJoin()
						.join(recruitment.recruitmentPositions, recruitmentPosition).fetchJoin()
						.fetchFirst()
		);
	}

	public boolean existsByStudyId(final Long studyId) {
		return q.select(recruitment.id)
				.from(recruitment)
				.where(recruitment.study.id.eq(studyId))
				.fetchFirst() != null;
	}

	public boolean existsById(final Long id) {
		return recruitmentJpaRepository.existsById(id);
	}

	public Recruitment save(final Recruitment recruitment) {
		return recruitmentJpaRepository.save(recruitment);
	}

	public void delete(final Recruitment recruitment) {
		recruitmentJpaRepository.delete(recruitment);
	}

	public List<Recruitment> findPopularRecruitments(final Long categoryId,
													 final PopularRecruitmentCond cond) {
		return q.select(recruitment)
				.from(recruitment)
				.join(recruitment.study, study)
				.where(study.category.id.eq(categoryId))
				.limit(cond.count())
				.orderBy(recruitment.hits.desc())
				.fetch();
	}

	public List<Recruitment> findRecruitments(final RecruitmentFindCursor recruitmentFindCursor,
											  final RecruitmentFindCond recruitmentFindCond) {

		JPAQuery<Recruitment> recruitmentTable = q.select(recruitment)
				.from(recruitment);

		if (isSatisfyJoinToStudy(recruitmentFindCond)) {
			recruitmentTable.innerJoin(recruitment.study, study);
		}

		if (isSatisfyJoinToRecruitmentPosition(recruitmentFindCond)) {
			recruitmentTable.innerJoin(recruitment.recruitmentPositions, recruitmentPosition);
		}

		if (isSatisfyJoinToRecruitmentStack(recruitmentFindCond)) {
			recruitmentTable.innerJoin(recruitment.recruitmentStacks, recruitmentStack);
		}

		return recruitmentTable
				.where(
						eqCategory(recruitmentFindCond.categoryId()),
						eqWay(recruitmentFindCond.way()),
						eqPosition(recruitmentFindCond.positionId()),
						eqStack(recruitmentFindCond.stackIds()))
				.orderBy(
						recruitment.modifiedDateTime.desc(),
						recruitment.id.desc())
				.where(lessThan(recruitmentFindCursor.last()))
				.limit(recruitmentFindCursor.count())
				.fetch();

	}

	private BooleanExpression eqStack(final List<Long> stackIds) {
		if (!isStackCondExist(stackIds)) {
			return null;
		}

		return stackIds.stream()
				.map(recruitmentStack.stack.id::eq)
				.reduce(BooleanExpression::or)
				.orElse(null);
	}

	private BooleanExpression eqCategory(final Long categoryId) {
		if (isCategoryCondExist(categoryId)) {
			return study.category.id.eq(categoryId);
		}
		return null;
	}

	private BooleanExpression eqWay(final String way) {
		if (isWayCondExist(way)) {
			return study.way.eq(Way.from(way));
		}
		return null;
	}

	private BooleanExpression eqPosition(final Long positionId) {
		if (isPositionCondExist(positionId)) {
			return recruitmentPosition.position.id.eq(positionId);
		}
		return null;
	}

	private boolean isSatisfyJoinToStudy(final RecruitmentFindCond recruitmentFindCond) {
		return isCategoryCondExist(recruitmentFindCond.categoryId())
				|| isWayCondExist(recruitmentFindCond.way());
	}

	private boolean isSatisfyJoinToRecruitmentPosition(final RecruitmentFindCond recruitmentFindCond) {
		return isPositionCondExist(recruitmentFindCond.positionId());
	}

	private boolean isSatisfyJoinToRecruitmentStack(final RecruitmentFindCond recruitmentFindCond) {
		return isStackCondExist(recruitmentFindCond.stackIds());
	}

	private boolean isCategoryCondExist(final Long categoryId) {
		return categoryId != null;
	}

	private boolean isWayCondExist(final String way) {
		return way != null;
	}

	private boolean isPositionCondExist(final Long positionId) {
		return positionId != null;
	}

	private boolean isStackCondExist(final List<Long> stackIds) {
		return stackIds != null && !stackIds.isEmpty();
	}

	private BooleanExpression lessThan(Long recruitmentId) {
		return recruitmentId != null ? recruitment.id.lt(recruitmentId) : null;
	}

}
