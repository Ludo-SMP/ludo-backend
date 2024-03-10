package com.ludo.study.studymatchingplatform.study.service.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponse;

@Component
public class RecruitmentPreviewResponseMapper {

	public List<RecruitmentPreviewResponse> mapBy(final List<Recruitment> recruitments) {
		return recruitments.stream()
				.map(recruitment -> {
					Study study = recruitment.getStudy();
					return getRecruitmentPreviewResponse(recruitment, study);
				})
				.toList();
	}

	private RecruitmentPreviewResponse getRecruitmentPreviewResponse(final Recruitment recruitment, final Study study) {
		return new RecruitmentPreviewResponse(
				recruitment.getId(),
				recruitment.getTitle(),
				recruitment.getCreatedDateTime().toString(),
				recruitment.getRecruitmentEndDateTime().toString(),
				recruitment.getHits(),
				recruitment.getStackNames(),
				recruitment.getPositionNames(),

				study.getCategoryByName(),
				study.getOwnerNickname(),
				study.getWay().toString(),
				study.getStartDateTime().toString(),
				study.getEndDateTime().toString()
		);
	}
}
