package com.ludo.study.studymatchingplatform.study.service.dto.request.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import lombok.Builder;

@Builder
public record WriteReviewRequest(
        Long revieweeId,
        Long activenessScore,
        Long professionalismScore,
        Long communicationScore,
        Long togetherScore,
        Long recommendScore
) {

    public Review toReviewWithStudy(final Study study, final User reviewer, final User reviewee) {
        return Review.builder()
                .study(study)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .activenessScore(activenessScore)
                .professionalismScore(professionalismScore)
                .communicationScore(communicationScore)
                .togetherScore(togetherScore)
                .recommendScore(recommendScore)
                .build();
    }
}
