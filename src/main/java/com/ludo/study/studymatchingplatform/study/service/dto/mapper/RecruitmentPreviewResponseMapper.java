package com.ludo.study.studymatchingplatform.study.service.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.common.ResourcePath;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;

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
				recruitment.getPositions().stream()
						.map(position -> new RecruitmentPreviewResponse.PositionDetail(position.getId(),
								position.getName()))
						.toList(),
				recruitment.getStacks().stream()
						.map(stack -> new RecruitmentPreviewResponse.StackDetail(stack.getId(), stack.getName(),
								ResourcePath.STACK_IMAGE.getPath() + stack.getImageUrl()))
						.toList(),

				new RecruitmentPreviewResponse.CategoryDetail(study.getCategoryId(), study.getCategoryByName()),
				study.getOwnerNickname(),
				study.getWay().toString(),
				recruitment.getHits(),
				recruitment.getRecruitmentEndDateTime(),
				study.getStartDateTime(),
				study.getEndDateTime(),
				recruitment.getCreatedDateTime()
		);
	}

}
