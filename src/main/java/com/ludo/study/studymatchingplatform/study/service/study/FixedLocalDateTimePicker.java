package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.common.utils.LocalDateTimePicker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Profile("test")
@Component
@RequiredArgsConstructor
public class FixedLocalDateTimePicker implements LocalDateTimePicker {

    public static final LocalDateTime DEFAULT_FIXED_LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
    private final LocalDateTime fixedLocalDateTime = DEFAULT_FIXED_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime now() {
        return fixedLocalDateTime;
    }
}
