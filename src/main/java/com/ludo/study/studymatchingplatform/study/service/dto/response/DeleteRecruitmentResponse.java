package com.ludo.study.studymatchingplatform.study.service.dto.response;

import lombok.Builder;

@Builder
public record DeleteRecruitmentResponse(

		boolean ok,
		String message

) {

	public static DeleteRecruitmentResponse from(final String message) {
		return DeleteRecruitmentResponse.builder()
				.ok(true)
				.message(message)
				.build();
	}

}
