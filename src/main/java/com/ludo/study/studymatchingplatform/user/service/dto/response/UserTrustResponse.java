package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;

import lombok.Builder;

@Builder
public record UserTrustResponse(
		Integer totalTeammateCount,
		Integer totalFinishAttendanceStudies,
		Integer totalPerfectAttendanceStudies,
		Integer totalLeftStudyCount,
		Double totalAttendanceAverage,
		Double activeness,
		Double professionalism,
		Double communication,
		Double together,
		Double recommend
) {

	public static UserTrustResponse from(final StudyStatistics statistics, final ReviewStatistics reviewStatistics) {
		return UserTrustResponse.builder()
				.totalTeammateCount(statistics.getTotalTeammateCount())
				.totalFinishAttendanceStudies(statistics.getTotalFinishAttendanceStudies())
				.totalPerfectAttendanceStudies(statistics.getTotalPerfectAttendanceStudies())
				.totalLeftStudyCount(statistics.getTotalLeftStudyCount())
				.totalAttendanceAverage(statistics.getTotalAttendanceAverage())
				.activeness(reviewStatistics.getActivenessPercentage())
				.professionalism(reviewStatistics.getProfessionalismPercentage())
				.communication(reviewStatistics.getCommunicationPercentage())
				.together(reviewStatistics.getTogetherPercentage())
				.recommend(reviewStatistics.getRecommendPercentage())
				.build();
	}

}
