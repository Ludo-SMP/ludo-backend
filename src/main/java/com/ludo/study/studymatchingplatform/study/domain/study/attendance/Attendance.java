package com.ludo.study.studymatchingplatform.study.domain.study.attendance;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDate;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

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
public class Attendance extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendance_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "study_id", nullable = false)
	private Study study;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	public static Attendance from(final Study study,
								  final User user,
								  final LocalDate date) {
		final Attendance attendance = new Attendance();
		attendance.study = study;
		attendance.user = user;
		attendance.date = date;
		return attendance;
	}

}
