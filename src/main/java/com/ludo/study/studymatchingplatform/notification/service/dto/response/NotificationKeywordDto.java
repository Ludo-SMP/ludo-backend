package com.ludo.study.studymatchingplatform.notification.service.dto.response;

import java.util.List;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;

public record NotificationKeywordDto(
		List<NotificationKeywordCategory> keywordCategories,
		List<NotificationKeywordStack> keywordStacks,
		List<NotificationKeywordPosition> keywordPositions
) {
}
