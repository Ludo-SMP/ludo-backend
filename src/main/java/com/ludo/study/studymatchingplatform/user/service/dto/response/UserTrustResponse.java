package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;

import lombok.Builder;

@Builder
public record UserTrustResponse(
		Integer finishStudy,
		Integer perfectStudy,
		Integer leftStudyCount, // 탈주한 스터디 카운트
		Integer accumulatedTeamMembers,
		Double averageAttendanceRate,
		Double activeness,
		Double professionalism,
		Double communication,
		Double together,
		Double recommend
) {

	public static UserTrustResponse from(final StudyStatistics statistics, final ReviewStatistics reviewStatistics) {
		return UserTrustResponse.builder()
				.finishStudy(statistics.getTotalFinishAttendanceStudies())
				.perfectStudy(statistics.getTotalPerfectAttendanceStudies())
				.leftStudyCount(statistics.getTotalLeftStudyCount())
				.accumulatedTeamMembers(statistics.getTotalTeammateCount())
				.averageAttendanceRate(statistics.getTotalAttendanceAverage())
				.activeness(reviewStatistics.getActivenessPercentage())
				.professionalism(reviewStatistics.getProfessionalismPercentage())
				.communication(reviewStatistics.getCommunicationPercentage())
				.together(reviewStatistics.getTogetherPercentage())
				.recommend(reviewStatistics.getRecommendPercentage())
				.build();
	}

}
