package com.ludo.study.studymatchingplatform.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class CurrentUtcDateTimePicker implements UtcDateTimePicker {

	@Override
	public LocalDateTime now() {
		return LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS);
	}
}
