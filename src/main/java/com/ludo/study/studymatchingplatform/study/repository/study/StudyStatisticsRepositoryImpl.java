package com.ludo.study.studymatchingplatform.study.repository.study;

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
		//        return q.selectFrom();
		return null;
	}

	public Optional<StudyStatistics> findByUserId(final Long userId) {
		return studyStatisticsJpaRepository.findById(userId);
	}

	public StudyStatistics save(final StudyStatistics statistics) {
		return studyStatisticsJpaRepository.save(statistics);
	}
}
