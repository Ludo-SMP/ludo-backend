package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public record RecruitmentDetailsResponse(
		Long id, String title, List<String> stacks, List<String> positions,
		String platformUrl, int applicantCount, String recruitmentEndDateTime,
		String content, String createdDateTime,
		String category, String ownerNickname, String way,
		String startDateTime, String endDateTime
) {

	public RecruitmentDetailsResponse(final Recruitment recruitment, final Study study) {

		this(
				recruitment.getId(),
				recruitment.getTitle(),
				recruitment.getStackNames(),
				recruitment.getPositionNames(),
				recruitment.getCallUrl(),
				recruitment.getRecruitmentLimit(),
				recruitment.getRecruitmentEndDateTime().toString(),
				recruitment.getContent(),
				recruitment.getCreatedDateTime().toString(),

				study.getCategoryByName(),
				study.getOwnerNickname(),
				study.getWay().name(),
				study.getStartDateTime().toString(),
				study.getEndDateTime().toString()
		);
	}

}
