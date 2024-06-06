package com.ludo.study.studymatchingplatform.notification.service;

import com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup;

public record NotificationConfigRequest(
		NotificationConfigGroup notificationConfigGroup,
		boolean on
) {
}
