package com.ludo.study.studymatchingplatform.notification.domain.notification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;

import lombok.Getter;

@Getter
public enum StudyEndDateNotifyCond {

	remainingPeriod(5L);

	private final Long period;

	StudyEndDateNotifyCond(Long period) {
		this.period = period;
	}

	public LocalDate getStudyEndDate(final UtcDateTimePicker utcDateTimePicker) {
		return utcDateTimePicker.now().toLocalDate().plusDays(getPeriod());
	}

	public LocalDateTime getStudyEndDateStartOfDay(final UtcDateTimePicker utcDateTimePicker,
												   final LocalDate studyEndDate
	) {
		return utcDateTimePicker.toMicroSeconds(studyEndDate.atStartOfDay());
	}

	public LocalDateTime getStudyEndDateEndOfDay(final UtcDateTimePicker utcDateTimePicker,
												 final LocalDate studyEndDate
	) {
		return utcDateTimePicker.toMicroSeconds(studyEndDate.atTime(LocalTime.MAX));
	}

}
