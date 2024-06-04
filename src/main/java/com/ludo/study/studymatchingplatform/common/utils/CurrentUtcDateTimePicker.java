package com.ludo.study.studymatchingplatform.common.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Profile("!test")
@Component
public class CurrentUtcDateTimePicker implements UtcDateTimePicker {

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS);
    }
}
