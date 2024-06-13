package com.ludo.study.studymatchingplatform.study.domain.study.participant;

import static jakarta.persistence.FetchType.*;

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

	@Column(nullable = false)
	private Integer totalAttendance;

	@Column(nullable = false)
	private Integer validAttendance;

	@Column(nullable = true)
	private LocalDateTime recentAttendanceDate;

	@Column(nullable = false)
	private LocalDateTime joiningDateTime; // 합류일

	public static Participant from(final Study study,
								   final User user,
								   final Position position,
								   final Role role,
								   final LocalDateTime joiningDateTime) {
		final Participant participant = new Participant();
		participant.study = study;
		participant.user = user;
		participant.role = role;
		participant.position = position;
		participant.totalAttendance = 0;
		participant.validAttendance = 0;
		participant.joiningDateTime = joiningDateTime;
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
		final boolean isMatchesUser = Objects.equals(this.user.getId(), user.getId());
		return isMatchesUser && !isDeleted();
	}

	public void leave(final Study study) {
		study.removeParticipant(this);
		this.study = null;
		this.softDelete();
	}

	public void updatePosition(final Position position) {
		this.position = position;
	}

	public void increaseTotalAttendance() {
		this.totalAttendance++;
	}

	public void increaseValidAttendance() {
		this.validAttendance++;
	}

	public void renewalAttendanceDate(final LocalDateTime now) {
		this.recentAttendanceDate = now;
	}

	public boolean isFirstAttendance() {
		if (this.recentAttendanceDate == null) { // 첫 번째 출석 체크인 경우
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
