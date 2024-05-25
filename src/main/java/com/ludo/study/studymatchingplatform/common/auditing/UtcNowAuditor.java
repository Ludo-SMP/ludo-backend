package com.ludo.study.studymatchingplatform.common.auditing;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public final class UtcNowAuditor {

    private final UtcDateTimePicker utcDateTimePicker;

    @PrePersist
    public void prePersistUtcNow(final Object target) throws IllegalAccessException {
        setUtcNowForAnnotatedFields(target, PrePersistUtcNow.class);
        setUtcNowForAnnotatedFields(target, PreUpdateUtcNow.class);
    }

    @PreUpdate
    public void preUpdateUtcNow(final Object target) throws IllegalAccessException {
        setUtcNowForAnnotatedFields(target, PreUpdateUtcNow.class);
    }

    private void setUtcNowForAnnotatedFields(final Object target, final Class<? extends Annotation> annotationClass) throws IllegalAccessException {
        final Field[] fields = target.getClass().getDeclaredFields();

        for (final Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                field.setAccessible(true);
                if (field.getType().isAssignableFrom(LocalDateTime.class)) {
                    field.set(target, utcDateTimePicker.now());
                }
            }
        }
    }

}
