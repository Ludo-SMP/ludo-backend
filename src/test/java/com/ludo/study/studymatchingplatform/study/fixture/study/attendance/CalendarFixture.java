package com.ludo.study.studymatchingplatform.study.fixture.study.attendance;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;

public class CalendarFixture {

	public static Calender createCalendars(final Study study,
										   final LocalDateTime calenderStartDateTime,
										   final LocalDateTime calenderEndDateTime,
										   final Boolean monday,
										   final Boolean tuesday,
										   final Boolean wednesday,
										   final Boolean thursday,
										   final Boolean friday,
										   final Boolean saturday,
										   final Boolean sunday) {
		return Calender.builder()
				.study(study)
				.calenderStartDateTime(calenderStartDateTime)
				.calenderEndDateTime(calenderEndDateTime)
				.monday(monday)
				.tuesday(tuesday)
				.wednesday(wednesday)
				.thursday(thursday)
				.friday(friday)
				.saturday(saturday)
				.sunday(sunday)
				.build();
	}

}
