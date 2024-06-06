package com.ludo.study.studymatchingplatform.study.repository.study;

import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewStatisticsRepository {

    private final JPAQueryFactory q;
    private final ReviewStatisticsJpaRepository reviewStatisticsJpaRepository;

    public Optional<ReviewStatistics> findByUserId(final Long userId) {
        return reviewStatisticsJpaRepository.findById(userId);
    }

    public ReviewStatistics save(final ReviewStatistics statistics) {
        return reviewStatisticsJpaRepository.save(statistics);
    }

}
