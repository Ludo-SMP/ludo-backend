package com.ludo.study.studymatchingplatform.common.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class LocalDateTimePickerImpl implements LocalDateTimePicker {

	public LocalDateTime now() {
		return LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
	}
    
}
