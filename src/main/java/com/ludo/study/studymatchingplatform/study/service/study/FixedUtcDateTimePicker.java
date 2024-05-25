package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Profile("test")
@Component
@RequiredArgsConstructor
public class FixedUtcDateTimePicker implements UtcDateTimePicker {

    public static final LocalDateTime DEFAULT_FIXED_UTC_DATE_TIME = defaultFixedUtcDateTime();
    private LocalDateTime fixedUtcDateTime = DEFAULT_FIXED_UTC_DATE_TIME;

    @Override
    public LocalDateTime now() {
        return fixedUtcDateTime;
    }

    private static LocalDateTime defaultFixedUtcDateTime() {
        return LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0);
    }

    public void plusSeconds(final long seconds) {
        fixedUtcDateTime = fixedUtcDateTime.plusSeconds(seconds);
    }

    public void plusMinutes(final long minutes) {
        fixedUtcDateTime = fixedUtcDateTime.plusMinutes(minutes);
    }

    public void plusHours(final long hours) {
        fixedUtcDateTime = fixedUtcDateTime.plusHours(hours);
    }

    public void plusDays(final long days) {
        fixedUtcDateTime = fixedUtcDateTime.plusDays(days);
    }

    public void minusSeconds(final long seconds) {
        fixedUtcDateTime = fixedUtcDateTime.minusSeconds(seconds);
    }

    public void minusMinutes(final long minutes) {
        fixedUtcDateTime = fixedUtcDateTime.minusMinutes(minutes);
    }

    public void minusHours(final long hours) {
        fixedUtcDateTime = fixedUtcDateTime.minusHours(hours);
    }

    public void minusDays(final long days) {
        fixedUtcDateTime = fixedUtcDateTime.minusDays(days);
    }

}
