package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.Builder;

@Builder
public record ApplicantUserResponse(

		Long id,
		String nickname,
		String email,
		PositionResponse position

) {

	public static ApplicantUserResponse from(final Applicant applicant) {
		final User user = applicant.getUser();
		final PositionResponse response = PositionResponse.from(applicant.getPosition());
		return ApplicantUserResponse.builder()
				.id(user.getId())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.position(response)
				.build();
	}

}
