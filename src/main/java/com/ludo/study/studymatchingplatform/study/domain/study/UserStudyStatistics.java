package com.ludo.study.studymatchingplatform.study.domain.study;


import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserStudyStatistics extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int totalTeammateCount;

    // 진행 완료 스터디(진행도 100%)
    private int totalCompletedStudyCount;

    // 완주 스터디(진행도 80% 이상)
    private int totalFinishedStudyCount;

    // 총 무단 탈주 스터디
    private int totalLeftStudyCount;

    // 총 누적 출석
    private int totalAttendance;

    // 총 유효 출석
    private int totalValidAttendance;

    // TODO: 동시에 여러 스터디를 진행하는 경우, 중복 count? 일수?
    // 전체 스터디 진행 기간
    private int totalStudyDays;

}
