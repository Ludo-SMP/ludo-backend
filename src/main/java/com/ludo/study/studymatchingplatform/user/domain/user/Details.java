package com.ludo.study.studymatchingplatform.user.domain.user;

import static jakarta.persistence.FetchType.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Details extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_details_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private Integer finishStudy; // 진행한 스터디

	@Column(nullable = false)
	private Integer perfectStudy; // 완주한 스터디

	@Column(nullable = false)
	private Integer accumulatedTeamMembers; // 누적 팀원 수

	@Column(nullable = false)
	private Double existingDayOfAttendance; // 기존 출석 일수

	@Column(nullable = false)
	private Double existingMandatoryDayOfAttendance; // 기존 의무 출석 일수

	@Column(nullable = false)
	private Integer activeness;

	@Column(nullable = false)
	private Integer professionalism;

	@Column(nullable = false)
	private Integer communication;

	@Column(nullable = false)
	private Integer together;

	@Column(nullable = false)
	private Integer recommend;

	public static Details from(final User user) {
		final Details details = new Details();
		details.user = user;
		details.finishStudy = 0;
		details.perfectStudy = 0;
		details.accumulatedTeamMembers = 0;
		details.existingDayOfAttendance = 0.0;
		details.existingMandatoryDayOfAttendance = 0.0;
		details.activeness = 0;
		details.professionalism = 0;
		details.communication = 0;
		details.together = 0;
		details.recommend = 0;
		return details;
	}

	public void makeAverageAttendanceRate(final Participant participant, final List<Calender> calenders) {
		makeExistingMandatoryDayOfAttendance(participant, calenders);
		makeExistingDayOfAttendance(participant);
	}

	private void makeExistingDayOfAttendance(final Participant participant) {
		this.existingDayOfAttendance = (participant.getValidAttendance() + 0.0);
	}

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
						this.existingMandatoryDayOfAttendance++;
					}
					break;
				case 2: // 화요일
					if (Boolean.TRUE.equals(calender.getTuesday())) {
						this.existingMandatoryDayOfAttendance++;
					}
					break;
				case 3: // 수요일
					if (Boolean.TRUE.equals(calender.getWednesday())) {
						this.existingMandatoryDayOfAttendance++;
					}
					break;
				case 4: // 목요일
					if (Boolean.TRUE.equals(calender.getThursday())) {
						this.existingMandatoryDayOfAttendance++;
					}
					break;
				case 5: // 금요일
					if (Boolean.TRUE.equals(calender.getFriday())) {
						this.existingMandatoryDayOfAttendance++;
					}
					break;
				case 6: // 토요일
					if (Boolean.TRUE.equals(calender.getSaturday())) {
						this.existingMandatoryDayOfAttendance++;
					}
					break;
				case 7: // 일요일
					if (Boolean.TRUE.equals(calender.getSunday())) {
						this.existingMandatoryDayOfAttendance++;
					}
					break;
			}
		}
	}

}
