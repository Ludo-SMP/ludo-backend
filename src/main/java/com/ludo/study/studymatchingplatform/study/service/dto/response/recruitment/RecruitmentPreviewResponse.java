package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.util.List;

public record RecruitmentPreviewResponse(Long id, String title, String createdDateTime,
										 String recruitmentEndDateTime, int hits,
										 List<String> stacks, List<String> positions,
										 String category, String ownerNickname, String way,
										 String startDateTime, String endDateTime) {

}
