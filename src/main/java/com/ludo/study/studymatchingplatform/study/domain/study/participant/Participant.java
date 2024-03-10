package com.ludo.study.studymatchingplatform.study.domain.study.participant;

import static jakarta.persistence.FetchType.*;

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
public class Participant extends BaseEntity {

	@EmbeddedId
	@Builder.Default
	private ParticipantId id = new ParticipantId();

	@ManyToOne(fetch = LAZY)
	@MapsId("studyId")
	@JoinColumn(
			name = "study_id",
			nullable = false
	)
	private Study study;

	@ManyToOne(fetch = LAZY)
	@MapsId("userId")
	@JoinColumn(
			name = "user_id",
			nullable = false
	)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "char(10)")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;

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
		final boolean isMatchesUser = Objects.equals(this.user.getId(), user.getId());
		return isMatchesUser && !isDeleted();
	}

	public void leave(final Study study) {
		study.removeParticipant(this);
		this.study = null;
		this.softDelete();
	}
}
