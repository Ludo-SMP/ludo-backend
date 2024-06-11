package com.ludo.study.studymatchingplatform.notification.repository.dto;

import java.time.LocalDateTime;

public record StudyReviewStartNotifierCond(LocalDateTime yesterdayStartOfDay,
										   LocalDateTime yesterdayEndOfDay) {
}
