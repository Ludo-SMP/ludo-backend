package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

public record StudyStatisticsResponse(
        Long id,
        UserResponse user,
        int totalTeammateCount,
        // 진행 완료 스터디
        int totalFinishAttendanceStudies,
        // 완주 스터디(진행도 80% 이상)
        int totalPerfectAttendanceStudies,
        // 총 무단 탈주 스터디
        int totalLeftStudyCount,
        // 총 누적 출석
        int totalAttendance,
        // 총 유효 출석
        int totalValidAttendance,
        // TODO: 동시에 여러 스터디를 진행하는 경우, 중복 count? 일수?
        // 전체 스터디 진행 기간
        int totalStudyDays
) {
    public static StudyStatisticsResponse from(final StudyStatistics statistics) {
        return new StudyStatisticsResponse(
                statistics.getId(),
                UserResponse.from(statistics.getUser()),
                statistics.getTotalTeammateCount(),
                statistics.getTotalFinishAttendanceStudies(),
                statistics.getTotalPerfectAttendanceStudies(),
                statistics.getTotalLeftStudyCount(),
                statistics.getTotalAttendance(),
                statistics.getTotalValidAttendance(),
                statistics.getTotalStudyDays()
        );
    }
}
