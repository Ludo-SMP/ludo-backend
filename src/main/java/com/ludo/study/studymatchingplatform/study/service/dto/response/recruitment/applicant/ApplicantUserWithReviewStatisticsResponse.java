package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.ReviewStatisticsResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import lombok.Builder;

@Builder
public record ApplicantUserWithReviewStatisticsResponse(

        Long id,
        String nickname,
        String email,
        PositionResponse position,
        ReviewStatisticsResponse reviewStatistics
) {

    public static ApplicantUserWithReviewStatisticsResponse from(final Applicant applicant, final ReviewStatistics reviewStatistics) {
        final User user = applicant.getUser();
        final PositionResponse response = PositionResponse.from(applicant.getPosition());
        return ApplicantUserWithReviewStatisticsResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .position(response)
                .reviewStatistics(ReviewStatisticsResponse.from(reviewStatistics))
                .build();
    }

}
