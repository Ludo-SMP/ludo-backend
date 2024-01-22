package com.ludo.study.studymatchingplatform.study.application.dto;

import java.util.ArrayList;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecruitmentDetailsResponse {

	private final Long id;
	private final String title;
	private final List<String> stacks = new ArrayList<>();
	private final List<String> positions = new ArrayList<>();
	private final String platformUrl;
	private final int applicantCount;
	private final String recruitmentEndDateTime;
	private final String content;
	private final String createdDateTime;

	private final String category;
	private final String ownerNickname;
	private final String way;
	private final String startDateTime;
	private final String endDateTime;

	public RecruitmentDetailsResponse(final Recruitment recruitment, final Study study) {
		this.id = recruitment.getId();
		this.title = recruitment.getTitle();
		this.stacks.addAll(recruitment.getStackNames());
		this.positions.addAll(recruitment.getPositionNames());
		this.platformUrl = recruitment.getCallUrl();
		this.applicantCount = recruitment.getRecruitmentCount();
		this.recruitmentEndDateTime = recruitment.getRecruitmentEndDateTime().toString();
		this.content = recruitment.getContent();
		this.createdDateTime = recruitment.getCreatedDateTime().toString();

		this.category = study.getCategoryByName();
		this.ownerNickname = study.getOwnerNickname();
		this.way = study.getWay().name();
		this.startDateTime = study.getStartDateTime().toString();
		this.endDateTime = study.getEndDateTime().toString();
	}

}
