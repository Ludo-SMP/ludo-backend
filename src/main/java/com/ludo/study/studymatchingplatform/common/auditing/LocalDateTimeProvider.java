package com.ludo.study.studymatchingplatform.common.auditing;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeProvider implements DateTimeProvider {
	@Override
	public Optional<TemporalAccessor> getNow() {
		return Optional.of(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
	}

}
