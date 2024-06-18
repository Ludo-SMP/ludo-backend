package com.ludo.study.studymatchingplatform.study.domain.study.attendance;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDate;
import java.util.List;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

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
public class Calender extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "calender_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	// @MapsId("studyId")
	@JoinColumn(name = "study_id", nullable = false)
	private Study study;

	@Column(nullable = false)
	private LocalDate calenderStartDateTime;

	@Column(nullable = false)
	private LocalDate calenderEndDateTime;

	// 초기값 false 설정
	@Column(nullable = false)
	private Boolean monday = false;

	@Column(nullable = false)
	private Boolean tuesday = false;

	@Column(nullable = false)
	private Boolean wednesday = false;

	@Column(nullable = false)
	private Boolean thursday = false;

	@Column(nullable = false)
	private Boolean friday = false;

	@Column(nullable = false)
	private Boolean saturday = false;

	@Column(nullable = false)
	private Boolean sunday = false;

	public static Calender from(final Study study,
								final LocalDate calenderStartDateTime,
								final LocalDate calenderEndDateTime) {
		final Calender calender = new Calender();
		calender.study = study;
		calender.calenderStartDateTime = calenderStartDateTime;
		calender.calenderEndDateTime = calenderEndDateTime;
		return calender;
	}

	public void checkValidAttendanceDay(final List<Integer> attendanceDay) {
		for (Integer day : attendanceDay) {
			switch (day) {
				case 1: // 월요일
					this.monday = true;
					break;
				case 2: // 화요일
					this.tuesday = true;
					break;
				case 3: // 수요일
					this.wednesday = true;
					break;
				case 4: // 목요일
					this.thursday = true;
					break;
				case 5: // 금요일
					this.friday = true;
					break;
				case 6: // 토요일
					this.saturday = true;
					break;
				case 7: // 일요일
					this.sunday = true;
					break;
			}
		}
	}

}
