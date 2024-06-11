package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ApplicantWithReviewStatisticsResponse(
        ApplicantStudyResponse study,
        List<ApplicantUserWithReviewStatisticsResponse> applicants
) {

    public static ApplicantWithReviewStatisticsResponse from(final Study study, final List<Applicant> applicants, final List<ReviewStatistics> reviewStatistics) {
        final ApplicantStudyResponse studyResponse = ApplicantStudyResponse.from(study);
        final List<ApplicantUserWithReviewStatisticsResponse> applicantsResponse = aggregateReviewStatistics(applicants, reviewStatistics);
        return new ApplicantWithReviewStatisticsResponse(studyResponse, applicantsResponse);
    }

    private static List<ApplicantUserWithReviewStatisticsResponse> aggregateReviewStatistics(final List<Applicant> applicants, final List<ReviewStatistics> reviewStatistics) {
        final Map<Long, Applicant> applicantMap = applicants.stream().collect(Collectors.toMap(
                applicant -> applicant.getUser().getId(),
                applicant -> applicant
        ));
        final Map<Long, ReviewStatistics> reviewStatisticsMap = reviewStatistics.stream().collect(Collectors.toMap(
                r -> r.getUser().getId(),
                r -> r
        ));

        final List<ApplicantUserWithReviewStatisticsResponse> res = new ArrayList<>();
        final Stream<Long> userIds = applicants.stream().map(a -> a.getUser().getId());
        userIds.forEach(userId -> {
            res.add(
                    ApplicantUserWithReviewStatisticsResponse.from(
                            applicantMap.get(userId),
                            reviewStatisticsMap.get(userId))
            );
        });

        return res;
    }
}
