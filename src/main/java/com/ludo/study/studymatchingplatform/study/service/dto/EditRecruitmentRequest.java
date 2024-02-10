package com.ludo.study.studymatchingplatform.study.service.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class EditRecruitmentRequest {

	private long studyId;

	private long recruitmentId;

	private String title;

	private String callUrl;

	private List<Long> stackIds;

	private List<Long> positionIds;

	private int hits;

	private int recruitmentLimit;

	private LocalDateTime recruitmentEndDateTime;

	private String content;

}
