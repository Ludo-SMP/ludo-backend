package com.ludo.study.studymatchingplatform.study.repository.study.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Attendance;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long> {

}
