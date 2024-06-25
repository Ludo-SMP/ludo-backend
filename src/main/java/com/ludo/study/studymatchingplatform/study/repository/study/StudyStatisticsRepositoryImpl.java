package com.ludo.study.studymatchingplatform.study.repository.study;

import static com.ludo.study.studymatchingplatform.study.domain.study.QStudyStatistics.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudyStatisticsRepositoryImpl {

	private final JPAQueryFactory q;
	private final StudyStatisticsJpaRepository studyStatisticsJpaRepository;

	public List<StudyStatistics> findByUserIdsIn(final List<Long> userIds) {
		return q.selectFrom(studyStatistics)
				.where(studyStatistics.user.id.in(userIds))
				.fetch();
	}

	public Optional<StudyStatistics> findByUserId(final Long userId) {
		return Optional.ofNullable(
				q.selectFrom(studyStatistics)
						.where(studyStatistics.user.id.eq(userId))
						.fetchFirst());
	}

	public StudyStatistics save(final StudyStatistics statistics) {
		return studyStatisticsJpaRepository.save(statistics);
	}
}
