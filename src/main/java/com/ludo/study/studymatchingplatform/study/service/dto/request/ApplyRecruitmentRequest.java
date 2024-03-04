package com.ludo.study.studymatchingplatform.study.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ApplyRecruitmentRequest(
		@Schema(description = "모집 공고 지원 시 기재할 포지션 id")
		Long positionId
) {

}
