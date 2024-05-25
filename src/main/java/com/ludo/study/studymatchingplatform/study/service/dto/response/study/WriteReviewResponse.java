package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

public record WriteReviewResponse(
        Long id,
        Long studyId,
        UserResponse reviewer,
        UserResponse reviewee,
        // 적극성
        Long activenessScore,
        // 전문성
        Long professionalismScore,
        // 의사소통
        Long communicationScore,
        // 다시함께
        Long togetherScore,
        // 추천 여부
        Long recommendScore
) {

    public static WriteReviewResponse from(final Review review) {
        return new WriteReviewResponse(
                review.getId(),
                review.getStudy().getId(),
                UserResponse.from(review.getReviewer()),
                UserResponse.from(review.getReviewee()),
                review.getActivenessScore(),
                review.getProfessionalismScore(),
                review.getCommunicationScore(),
                review.getTogetherScore(),
                review.getRecommendScore()
        );
    }
}
