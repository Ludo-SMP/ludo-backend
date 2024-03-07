package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.user.domain.User;

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
