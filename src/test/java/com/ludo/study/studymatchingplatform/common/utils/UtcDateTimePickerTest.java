package com.ludo.study.studymatchingplatform.common.utils;

import com.ludo.study.studymatchingplatform.study.service.study.FixedUtcDateTimePicker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UtcDateTimePickerTest {

    @Test
    @DisplayName("CurrentUtcDateTimePicker은 UTC+00 기준 현재 시간을 반환한다.")
    void testCurrentUtcDateTimePickerNow() {
        CurrentUtcDateTimePicker utcDateTimePickerImpl = new CurrentUtcDateTimePicker();
        log.info("now = {}", utcDateTimePickerImpl.now());

        FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        log.info("now = {}", fixedUtcDateTimePicker.now());

        var now = fixedUtcDateTimePicker.now();
        var due = now.plusDays(3);
        log.info("{}", now.isBefore(now));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker은 UTC+00 기준 2000-01-01을 반환한다.")
    void testFixedUtcDateTimePickerNow() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                2000,
                1,
                1,
                0,
                0,
                0,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker plusSeconds 실행 이후, now 호출 시, 반환값 변경")
    void testUpdateFixedUtcDateTimePickerNow() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.plusSeconds(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                2000,
                1,
                1,
                0,
                0,
                10,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker plusMinutes 실행 이후, now 호출 시, 반환값 변경")
    void testUpdateFixedUtcDateTimePickeNow2() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.plusMinutes(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                2000,
                1,
                1,
                0,
                10,
                0,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker plusSeconds 실행 이후, now 호출 시, 반환값 변경")
    void testFixedUtcDateTimePickerNow2() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.plusSeconds(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                2000,
                1,
                1,
                0,
                0,
                10,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker plusHours 실행 이후, now 호출 시, 반환값 변경")
    void testFixedUtcDateTimePickerNow3() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.plusHours(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                2000,
                1,
                1,
                10,
                0,
                0,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker plusDays 실행 이후, now 호출 시, 반환값 변경")
    void testFixedUtcDateTimePickerNow4() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.plusDays(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                2000,
                1,
                11,
                0,
                0,
                0,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker minusSeconds 실행 이후, now 호출 시, 반환값 변경")
    void testUpdateFixedUtcDateTimePickerNow5() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.minusSeconds(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                1999,
                12,
                31,
                23,
                59,
                50,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker minusMinutes 실행 이후, now 호출 시, 반환값 변경")
    void testUpdateFixedUtcDateTimePickeNow6() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.minusMinutes(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                1999,
                12,
                31,
                23,
                50,
                0,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker minusHours 실행 이후, now 호출 시, 반환값 변경")
    void testFixedUtcDateTimePickerNow7() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.minusHours(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                1999,
                12,
                31,
                14,
                0,
                0,
                0
        ));
    }

    @Test
    @DisplayName("FixedUtcDateTimePicker minusDays 실행 이후, now 호출 시, 반환값 변경")
    void testFixedUtcDateTimePickerNow8() {
        final FixedUtcDateTimePicker fixedUtcDateTimePicker = new FixedUtcDateTimePicker();
        fixedUtcDateTimePicker.minusDays(10);
        final LocalDateTime now = fixedUtcDateTimePicker.now();

        assertThat(now).isEqualTo(LocalDateTime.of(
                1999,
                12,
                22,
                0,
                0,
                0,
                0
        ));
    }

}