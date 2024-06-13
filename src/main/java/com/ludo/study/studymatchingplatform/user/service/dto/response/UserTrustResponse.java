package com.ludo.study.studymatchingplatform.user.service.dto.response;

import com.ludo.study.studymatchingplatform.user.domain.user.Details;

import lombok.Builder;

@Builder
public record UserTrustResponse(
		Integer finishStudy,
		Integer perfectStudy,
		Integer accumulatedTeamMembers,
		Double averageAttendanceRate,
		Integer activeness,
		Integer professionalism,
		Integer communication,
		Integer together,
		Integer recommend
) {

	// 추가 항목 작성 필요.
	public static UserTrustResponse from(final Details details) {
		final Double average =
				(details.getExistingDayOfAttendance() / details.getExistingMandatoryDayOfAttendance()) * 100;
		return UserTrustResponse.builder()
				.finishStudy(1)
				.perfectStudy(1)
				.accumulatedTeamMembers(1)
				.averageAttendanceRate(average)
				.activeness(1)
				.professionalism(1)
				.communication(1)
				.together(1)
				.recommend(1)
				.build();
	}

}
