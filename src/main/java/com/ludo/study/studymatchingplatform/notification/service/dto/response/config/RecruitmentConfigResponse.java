package com.ludo.study.studymatchingplatform.notification.service.dto.response.config;

import java.util.List;

public record RecruitmentConfigResponse(String type,
										boolean on,
										List<CategoryKeyword> categoryKeywords,
										List<PositionKeyword> positionKeywords,
										List<StackKeyword> stackKeywords
) {

	public record CategoryKeyword(Long categoryId, String name) {
	}

	public record PositionKeyword(Long positionId, String name) {
	}

	public record StackKeyword(Long stackId, String name) {
	}
}