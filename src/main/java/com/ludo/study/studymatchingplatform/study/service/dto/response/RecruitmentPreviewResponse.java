package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RecruitmentPreviewResponse(Long id, String title, List<PositionDetail> positions,
										 List<StackDetail> stacks, CategoryDetail category,
										 String ownerNickname, String way, Integer hits,
										 LocalDateTime recruitmentEndDateTime, LocalDateTime startDateTime,
										 LocalDateTime endDateTime, LocalDateTime createdDateTime) {

	public record StackDetail(Long id, String name, String imageUrl) {
	}

	public record PositionDetail(Long id, String name) {
	}

	public record CategoryDetail(Long id, String name) {
	}

}
