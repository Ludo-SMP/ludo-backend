package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;

public record ReviewResponse(
        Long reviewerId,
        Long revieweeId,
        Long activenessScore,
        Long professionalismScore,
        Long communicationScore,
        Long togetherScore,
        Long recommendScore
) {
    public static ReviewResponse from(final Review review) {
        return new ReviewResponse(
                review.getReviewer().getId(),
                review.getReviewee().getId(),
                review.getActivenessScore(),
                review.getProfessionalismScore(),
                review.getCommunicationScore(),
                review.getTogetherScore(),
                review.getRecommendScore()
        );
    }
}
