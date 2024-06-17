package com.ludo.study.studymatchingplatform.study.domain.study;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyStatistics extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_statistics_id")
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder.Default
	private int totalTeammateCount = 0;

	// 진행 완료 스터디(진행도 100%)
	@Builder.Default
	private int totalFinishAttendanceStudies = 0;

	// 완주 스터디(진행도 80% 이상)
	@Builder.Default
	private int totalPerfectAttendanceStudies = 0;

	// 총 무단 탈주 스터디
	@Builder.Default
	private int totalLeftStudyCount = 0;

	// 총 누적 출석
	@Builder.Default
	private int totalAttendance = 0;

	// 총 유효 출석
	@Builder.Default
	private int totalValidAttendance = 0;

	// TODO: 동시에 여러 스터디를 진행하는 경우, 중복 count? 일수?
	// 전체 스터디 진행 기간
	@Builder.Default
	private int totalStudyDays = 0;

	public static StudyStatistics of(final User user) {
		return StudyStatistics.builder()
				.user(user)
				.build();
	}

	public void reflectStatistics(final Study study, final Participant participant) {
		// TODO:
		// 전체 스터디 진행 기간 -> 스터디 끝날 때마다 추가

		// totalStudyDays 전체 스터디 진행 기간은 자동 출석 체크 기능의 테이블이 만들어진 뒤 해당 API를 사용하여 데이터 저장 필요
		// this.totalStudyDays += ;

		totalAttendance += participant.getAttendance();
		totalValidAttendance += participant.getValidAttendance();
		totalTeammateCount += study.getParticipantCount() - 1;
		if (participant.finishAttendance()) {
			totalFinishAttendanceStudies++;
		}
		if (participant.perfectAttendance()) {
			totalPerfectAttendanceStudies++;
		}
	}

}
