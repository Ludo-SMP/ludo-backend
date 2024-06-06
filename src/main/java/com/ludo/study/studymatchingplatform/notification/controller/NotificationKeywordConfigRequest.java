package com.ludo.study.studymatchingplatform.notification.controller;

import java.util.List;

public record NotificationKeywordConfigRequest(
		List<Long> categoryIds,
		List<Long> stackIds,
		List<Long> positionIds
) {
}
