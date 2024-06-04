package com.ludo.study.studymatchingplatform.common.auditing;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UtcNowProvider implements DateTimeProvider {

    private final UtcDateTimePicker utcDateTimePicker;

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(utcDateTimePicker.now());
//		return Optional.of(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
    }

}
