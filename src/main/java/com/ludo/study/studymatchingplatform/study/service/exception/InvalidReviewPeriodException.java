package com.ludo.study.studymatchingplatform.study.service.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class InvalidReviewPeriodException extends IllegalArgumentException {

    private static final String MESSAGE = "%s\n리뷰 작성은 스터디 완료 3일 후인 %s부터 14일 후인 %s까지 가능합니다.";

    public InvalidReviewPeriodException(final String message, final LocalDateTime reviewAvailStartTime, final LocalDateTime reviewAvailEndTime) {
        super(MESSAGE.formatted(message, formatAvailTime(reviewAvailStartTime), formatAvailTime((reviewAvailEndTime))));
    }

    private static String formatAvailTime(final LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분", Locale.KOREA));
    }
}
