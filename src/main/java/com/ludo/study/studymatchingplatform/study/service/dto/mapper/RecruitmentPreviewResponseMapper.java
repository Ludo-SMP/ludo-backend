package com.ludo.study.studymatchingplatform.study.service.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.common.ResourcePath;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
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
		log.info("recruitment.getStudy() start");
		final Study study = recruitment.getStudy();
		log.info("recruitment.getStudy() end");
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
		log.info("mapToPositionDetail start");
		log.info("recruitment.getPositions() start");
		final List<Position> positions = recruitment.getPositions();
		log.info("recruitment.getPositions() end");
		final List<RecruitmentPreviewResponse.PositionDetail> result = positions
				.stream()
				.map(position -> new RecruitmentPreviewResponse.PositionDetail(position.getId(), position.getName()))
				.toList();
		log.info("mapToPositionDetail end");
		return result;
	}

	private List<RecruitmentPreviewResponse.StackDetail> mapToStackDetail(Recruitment recruitment) {
		log.info("mapToStackDetail start");
		log.info("recruitment.getStacks() start");
		final List<Stack> stacks = recruitment.getStacks();
		log.info("recruitment.getStacks() end");
		final List<RecruitmentPreviewResponse.StackDetail> result = stacks
				.stream()
				.map(stack -> new RecruitmentPreviewResponse.StackDetail(
						stack.getId(),
						stack.getName(),
						ResourcePath.STACK_IMAGE.getPath() + stack.getImageUrl()))
				.toList();
		log.info("mapToStackDetail end");
		return result;
	}

	private RecruitmentPreviewResponse.CategoryDetail mapToCategoryDetail(Study study) {
		log.info("mapToCategoryDetail start");
		log.info("study.getCategory() start");
		final Category category = study.getCategory();
		log.info("study.getCategory() end");
		final RecruitmentPreviewResponse.CategoryDetail result = new RecruitmentPreviewResponse.CategoryDetail(
				category.getId(), category.getName());
		log.info("mapToCategoryDetail end");
		return result;
	}

}
