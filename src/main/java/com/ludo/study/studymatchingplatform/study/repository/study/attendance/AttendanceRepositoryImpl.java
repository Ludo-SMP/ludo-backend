package com.ludo.study.studymatchingplatform.study.repository.study.attendance;

import static com.ludo.study.studymatchingplatform.study.domain.study.attendance.QAttendance.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Attendance;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl {

	private final JPAQueryFactory q;

	private final AttendanceJpaRepository attendanceJpaRepository;

	public Attendance save(final Attendance attendance) {
		return attendanceJpaRepository.save(attendance);
	}

	public Optional<Attendance> findMaxIdByStudyIdAndUserId(final Long studyId, final Long userId) {
		return Optional.ofNullable(q.selectFrom(attendance)
				.where(attendance.study.id.eq(studyId))
				.where(attendance.user.id.eq(userId))
				.orderBy(attendance.id.desc())
				.fetchFirst());
	}

	public Optional<List<Attendance>> findByStudyIdAndUserId(final Long studyId, final Long userId) {
		return Optional.ofNullable(q.selectFrom(attendance)
				.where(attendance.study.id.eq(studyId))
				.where(attendance.user.id.eq(userId))
				.fetch());
	}

}
