package com.ludo.study.studymatchingplatform.study.service.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.common.ResourcePath;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RecruitmentPreviewResponseMapper {

	public List<RecruitmentPreviewResponse> mapBy(final List<Recruitment> recruitments) {
		return recruitments.stream()
				.map(this::getRecruitmentPreviewResponse)
				.toList();
	}

	private RecruitmentPreviewResponse getRecruitmentPreviewResponse(final Recruitment recruitment) {
		final Study study = recruitment.getStudy();
		return new RecruitmentPreviewResponse(
				recruitment.getId(),
				recruitment.getTitle(),
				mapToPositionDetail(recruitment),
				mapToStackDetail(recruitment),
				mapToCategoryDetail(study),
				study.getOwnerNickname(),
				study.getWay().toString(),
				recruitment.getHits(),
				recruitment.getRecruitmentEndDateTime(),
				study.getStartDateTime(),
				study.getEndDateTime(),
				recruitment.getCreatedDateTime()
		);
	}

	private List<RecruitmentPreviewResponse.PositionDetail> mapToPositionDetail(Recruitment recruitment) {
		final List<Position> positions = recruitment.getPositions();
		return positions.stream()
				.map(position -> new RecruitmentPreviewResponse.PositionDetail(position.getId(), position.getName()))
				.toList();
	}

	private List<RecruitmentPreviewResponse.StackDetail> mapToStackDetail(Recruitment recruitment) {
		final List<Stack> stacks = recruitment.getStacks();
		return stacks.stream()
				.map(stack -> new RecruitmentPreviewResponse.StackDetail(
						stack.getId(),
						stack.getName(),
						ResourcePath.STACK_IMAGE.getPath() + stack.getImageUrl()))
				.toList();
	}

	private RecruitmentPreviewResponse.CategoryDetail mapToCategoryDetail(Study study) {
		final Category category = study.getCategory();
		return new RecruitmentPreviewResponse.CategoryDetail(
				category.getId(), category.getName());
	}

}
