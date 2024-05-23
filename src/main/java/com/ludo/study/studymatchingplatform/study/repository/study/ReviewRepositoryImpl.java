package com.ludo.study.studymatchingplatform.study.repository.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ludo.study.studymatchingplatform.study.domain.study.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl {

    private final JPAQueryFactory q;

    private final ReviewJpaRepository reviewJpaRepository;

    public List<Review> findAllByStudyId(final Long studyId) {
        return q.selectFrom(review)
                .where(review.study.id.eq(studyId)
                        .and(review.deletedDateTime.isNull()))
                .fetch();
    }

    public Review save(final Review review) {
        return reviewJpaRepository.save(review);
    }

    public boolean existsBy(final Long studyId, final Long reviewerId, final Long revieweeId) {
//        q.select(review)
//                .fetchFirst();
        return false;
    }

}
