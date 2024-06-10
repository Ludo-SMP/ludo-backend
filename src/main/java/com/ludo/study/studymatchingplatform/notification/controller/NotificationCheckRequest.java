package com.ludo.study.studymatchingplatform.notification.controller;

import java.util.List;

public record NotificationCheckRequest(List<Long> notificationIds) {
}
