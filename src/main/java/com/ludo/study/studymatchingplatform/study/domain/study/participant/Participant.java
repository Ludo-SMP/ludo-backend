package com.ludo.study.studymatchingplatform.study.domain.study.participant;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.id.ParticipantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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
@Table(name = "study_user_lnk")
public class Participant extends BaseEntity {

	@EmbeddedId
	@Builder.Default
	private ParticipantId id = new ParticipantId();

	@ManyToOne(fetch = LAZY)
	@MapsId("studyId")
	@JoinColumn(name = "study_id", nullable = false)
	private Study study;

	@ManyToOne(fetch = LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "char(10)")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;

	// 누적 출석
	@Builder.Default
	private int attendance = 0;

	// 누적 유효 출석
	@Builder.Default
	private int validAttendance = 0;

	@Column(nullable = true)
	private LocalDate recentAttendanceDate;

	// 스터디 합류일
	@Builder.Default
	private LocalDate enrollmentDateTime = LocalDateTime.now().toLocalDate();

	public static Participant from(final Study study, final User user, final Position position, final Role role) {
		final Participant participant = new Participant();
		participant.study = study;
		participant.user = user;
		participant.role = role;
		participant.position = position;
		return participant;
	}

	public Role getRole() {
		// TODO: Role spec not determined clearly
		// TODO: First of all, I reflected it in my own way
		if (study.isOwner(this)) {
			return Role.OWNER;
		}
		return Role.MEMBER;
	}

	public boolean matchesUser(final User user) {
		return matchesUser(user.getId());
	}

	public boolean matchesUser(final Long userId) {
		final boolean isMatchesUser = Objects.equals(this.user.getId(), userId);
		return isMatchesUser && !isDeleted();
	}

	public void leave(final Study study, final LocalDateTime deletedDateTime) {
		study.removeParticipant(this);
		this.study = null;
		this.softDelete(deletedDateTime);
	}

	public void updatePosition(final Position position) {
		this.position = position;
	}

	public void increaseTotalAttendance() {
		this.attendance++;
	}

	public void increaseValidAttendance() {
		this.validAttendance++;
	}

	public void renewalAttendanceDate(final LocalDate now) {
		this.recentAttendanceDate = now;
	}

	public boolean isFirstAttendance() {
		if (this.recentAttendanceDate == null) { // 첫 번째 출석 체크인 경우
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean finishAttendance() {
		// TODO:
		return true;
	}

	// 출석 80% 이상
	public boolean perfectAttendance() {
		// TODO: total 스터디 일수를 가져오는 API 필요. 우선 임시 변수로 저장
		int totalStudyDays = 100;
		return (attendance / totalStudyDays * 100) > 80;
	}

}
