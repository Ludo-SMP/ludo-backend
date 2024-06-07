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

    public boolean exists(final Long studyId, final Long reviewerId, final Long revieweeId) {
        return q.selectOne()
                .from(review)
                .where(review.study.id.eq(studyId)
                        .and(review.reviewer.id.eq(reviewerId))
                        .and(review.reviewee.id.eq(revieweeId))
                )
                .fetchFirst() != null;
    }

    public List<Review> findAllSelfReviews(final Long reviewerId) {
        return q.selectFrom(review)
                .where(review.reviewer.id.eq(reviewerId))
                .fetch();
    }

    public List<Review> findAllPeerReviews(final Long revieweeId) {
        return q.selectFrom(review)
                .where(review.reviewee.id.eq(revieweeId))
                .fetch();
    }

    public List<Review> findSelfReviews(final Long studyId, final Long selfId, final List<Long> peerIds) {
        return q.selectFrom(review)
                .where(review.study.id.eq(studyId)
                        .and(review.reviewer.id.eq(selfId))
                        .and(review.reviewee.id.in(peerIds)))
                .fetch();
    }

    public List<Review> findPeerReviews(final Long studyId, final Long selfId, final List<Long> peerIds) {
        return q.selectFrom(review)
                .where(review.study.id.eq(studyId)
                        .and(review.reviewer.id.in(peerIds))
                        .and(review.reviewee.id.eq(selfId)))
                .fetch();
    }

}
