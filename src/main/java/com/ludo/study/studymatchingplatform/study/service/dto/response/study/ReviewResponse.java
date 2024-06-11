package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewerId,
        Long revieweeId,
        Long activeness,
        Long professionalism,
        Long communication,
        Long together,
        Long recommend,
        LocalDateTime createdDateTime
) {
    public static ReviewResponse from(final Review review) {
        return new ReviewResponse(
                review.getReviewer().getId(),
                review.getReviewee().getId(),
                review.getActivenessScore(),
                review.getProfessionalismScore(),
                review.getCommunicationScore(),
                review.getTogetherScore(),
                review.getRecommendScore(),
                review.getCreatedDateTime()
        );
    }
}
