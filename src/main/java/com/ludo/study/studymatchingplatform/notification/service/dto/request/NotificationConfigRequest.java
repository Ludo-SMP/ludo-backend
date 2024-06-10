package com.ludo.study.studymatchingplatform.notification.service.dto.request;

import com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup;

public record NotificationConfigRequest(
		NotificationConfigGroup notificationConfigGroup,
		boolean on
) {
}
