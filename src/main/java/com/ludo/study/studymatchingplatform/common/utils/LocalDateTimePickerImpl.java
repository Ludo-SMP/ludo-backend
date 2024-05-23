package com.ludo.study.studymatchingplatform.common.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Profile("!test")
@Component
public class LocalDateTimePickerImpl implements LocalDateTimePicker {

    public LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
        return LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
        return LocalDateTime.now();
    }
}
