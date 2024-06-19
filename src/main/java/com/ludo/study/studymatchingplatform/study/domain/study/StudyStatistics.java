package com.ludo.study.studymatchingplatform.study.domain.study;

import static jakarta.persistence.FetchType.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
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
	private Integer totalTeammateCount = 0;

	// 진행 완료 스터디(진행 완료까지 함께한)
	@Builder.Default
	private Integer totalFinishAttendanceStudies = 0;

	// 완주 스터디(진행도 80% 이상)
	@Builder.Default
	private Integer totalPerfectAttendanceStudies = 0;

	// 총 무단 탈주 스터디, 필요하다면 진행 완료 스터디와 병합 가능
	@Builder.Default
	private Integer totalLeftStudyCount = 0;

	// 총 누적 출석 (의무 출석 기간)
	@Builder.Default
	private Integer totalMandatoryAttendance = 0;

	// 총 유효 출석
	@Builder.Default
	private Integer totalValidAttendance = 0;

	// TODO: 동시에 여러 스터디를 진행하는 경우, 중복 count? 일수?
	// 전체 스터디 기간은 디자인 레이아웃에서 사용되고 있지 않습니다.
	// 전체 스터디 진행 기간
	// @Builder.Default
	// private Integer totalStudyDays = 0;

	public static StudyStatistics of(final User user) {
		return StudyStatistics.builder()
				.user(user)
				.build();
	}

	public void reflectStatistics(final Study study, final Participant participant, final List<Calender> calenders) {
		// TODO: totalStudyDays 추가
		// 전체 스터디 진행 기간 -> 스터디 끝날 때마다 추가

		// totalStudyDays 전체 스터디 진행 기간은 자동 출석 체크 기능의 테이블이 만들어진 뒤 해당 API를 사용하여 데이터 저장 필요
		// this.totalStudyDays += ;

		// totalAttendance += participant.getAttendance(); // 의무 출석기간 되어야 함
		// totalValidAttendance += participant.getValidAttendance();

		makeExistingMandatoryDayOfAttendance(participant, calenders); // 스터디 합류일 기준 의무 출석일 산출
		makeExistingDayOfAttendance(participant); // 유효 출석일 산출

		totalTeammateCount += study.getParticipantCount() - 1;
		if (participant.finishAttendance()) {
			totalFinishAttendanceStudies++;
		}
		// 80퍼센트 이상 출석시
		if (participant.perfectAttendance()) {
			totalPerfectAttendanceStudies++;
		}
	}

	public Double getTotalAttendanceAverage() {
		return calculatePercentage(totalMandatoryAttendance, totalValidAttendance);
	}

	private Double calculatePercentage(final Integer totalMandatoryAttendance, final Integer totalValidAttendance) {
		if (totalValidAttendance == 0) {
			return 0.0;
		}
		return ((totalValidAttendance + 0.0) / totalMandatoryAttendance) * 100;
	}

	private void makeExistingDayOfAttendance(final Participant participant) {
		this.totalValidAttendance += participant.getValidAttendance();
	}

	// 의무 출석 기간 산출
	private void makeExistingMandatoryDayOfAttendance(final Participant participant, final List<Calender> calenders) {
		final LocalDate joiningDateTime = participant.getEnrollmentDateTime();
		final DayOfWeek joiningDateTimeOfWeek = joiningDateTime.getDayOfWeek();
		final Integer joiningDateTimeOfWeekNumber = joiningDateTimeOfWeek.getValue();

		for (Calender calender : calenders) {
			if (joiningDateTime.isAfter(calender.getCalenderStartDateTime())
					&& joiningDateTime.isBefore(calender.getCalenderEndDateTime())) { // 첫번째 카운트
				makeExistingMandatoryDayOfAttendanceCount(calender, joiningDateTimeOfWeekNumber);
				continue;
			}
			makeExistingMandatoryDayOfAttendanceCount(calender, 1); // 두번째 부터 카운트
		}
	}

	private void makeExistingMandatoryDayOfAttendanceCount(final Calender calender,
														   final Integer DateTimeOfWeekNumber) {
		for (int i = DateTimeOfWeekNumber; i <= 7; i++) {
			switch (i) {
				case 1: // 월요일
					if (Boolean.TRUE.equals(calender.getMonday())) {
						this.totalMandatoryAttendance++;
					}
					break;
				case 2: // 화요일
					if (Boolean.TRUE.equals(calender.getTuesday())) {
						this.totalMandatoryAttendance++;
					}
					break;
				case 3: // 수요일
					if (Boolean.TRUE.equals(calender.getWednesday())) {
						this.totalMandatoryAttendance++;
					}
					break;
				case 4: // 목요일
					if (Boolean.TRUE.equals(calender.getThursday())) {
						this.totalMandatoryAttendance++;
					}
					break;
				case 5: // 금요일
					if (Boolean.TRUE.equals(calender.getFriday())) {
						this.totalMandatoryAttendance++;
					}
					break;
				case 6: // 토요일
					if (Boolean.TRUE.equals(calender.getSaturday())) {
						this.totalMandatoryAttendance++;
					}
					break;
				case 7: // 일요일
					if (Boolean.TRUE.equals(calender.getSunday())) {
						this.totalMandatoryAttendance++;
					}
					break;
			}
		}
	}

}
