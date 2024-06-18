package com.ludo.study.studymatchingplatform.study.repository.study;

import static com.ludo.study.studymatchingplatform.study.domain.study.QReviewStatistics.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewStatisticsRepositoryImpl {

	private final JPAQueryFactory q;
	private final ReviewStatisticsJpaRepository reviewStatisticsJpaRepository;

	public Optional<ReviewStatistics> findByUserId(final Long userId) {
		return Optional.ofNullable(
				q.selectFrom(reviewStatistics)
						.where(reviewStatistics.user.id.eq(userId))
						.fetchFirst());
	}

	public ReviewStatistics save(final ReviewStatistics statistics) {
		return reviewStatisticsJpaRepository.save(statistics);
	}

}
