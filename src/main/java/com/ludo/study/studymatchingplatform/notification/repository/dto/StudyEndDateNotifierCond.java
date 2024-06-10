package com.ludo.study.studymatchingplatform.notification.repository.dto;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;

public record StudyEndDateNotifierCond(Role role, LocalDateTime endDateStartOfDay, LocalDateTime endDateEndOfDay) {

}
