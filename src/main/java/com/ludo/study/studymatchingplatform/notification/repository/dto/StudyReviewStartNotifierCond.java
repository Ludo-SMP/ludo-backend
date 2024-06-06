package com.ludo.study.studymatchingplatform.notification.repository.dto;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;

public record StudyReviewStartNotifierCond(LocalDateTime startOfDay,
										   LocalDateTime endOfDay,
										   StudyStatus studyStatus) {
}
