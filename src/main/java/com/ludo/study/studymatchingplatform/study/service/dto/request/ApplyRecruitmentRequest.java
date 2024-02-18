package com.ludo.study.studymatchingplatform.study.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ApplyRecruitmentRequest {

	private final long studyId;

}
