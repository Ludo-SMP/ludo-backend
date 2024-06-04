package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;

public record ReviewStatisticsResponse(
        // 적극성
        double activenessScore,
        // 전문성
        double professionalismScore,
        // 의사소통
        double communicationScore,
        // 다시함께
        double TogetherScore,
        // 추천
        double recommendationScore
) {
    public static ReviewStatisticsResponse from(final ReviewStatistics statistics) {
        return new ReviewStatisticsResponse(
                statistics.getActivenessPercentage(),
                statistics.getProfessionalismPercentage(),
                statistics.getCommunicationPercentage(),
                statistics.getTogetherPercentage(),
                statistics.getRecommendationPercentage()
        );
    }
}
