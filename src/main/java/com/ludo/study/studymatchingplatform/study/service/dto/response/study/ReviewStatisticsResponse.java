package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;

public record ReviewStatisticsResponse(
        // 적극성
        double activeness,
        // 전문성
        double professionalism,
        // 의사소통
        double communication,
        // 다시함께
        double together,
        // 추천
        double recommend
) {
    public static ReviewStatisticsResponse from(final ReviewStatistics statistics) {
        return new ReviewStatisticsResponse(
                statistics.getActivenessPercentage(),
                statistics.getProfessionalismPercentage(),
                statistics.getCommunicationPercentage(),
                statistics.getTogetherPercentage(),
                statistics.getRecommendPercentage()
        );
    }
}
