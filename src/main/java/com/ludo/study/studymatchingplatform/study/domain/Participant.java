package com.ludo.study.studymatchingplatform.study.domain;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.id.ParticipantId;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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

	public static Participant from(final Study study, final User user) {
		final Participant participant = new Participant();
		participant.study = study;
		participant.user = user;

		return participant;
	}

}
