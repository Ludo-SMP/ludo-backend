package com.ludo.study.studymatchingplatform.study.fixture.study.attendance;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Attendance;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public class AttendanceFixture {

	public static Attendance createAttendance(final Study study,
											  final User user,
											  final LocalDateTime date) {
		return Attendance.builder()
				.study(study)
				.user(user)
				.date(date)
				.build();
	}

}
