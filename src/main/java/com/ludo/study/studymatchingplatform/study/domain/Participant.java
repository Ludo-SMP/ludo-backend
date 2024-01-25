package com.ludo.study.studymatchingplatform.study.domain;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.id.ParticipantId;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Participant extends BaseEntity {
	@EmbeddedId
	private ParticipantId id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "study_id",
		insertable = false,
		updatable = false
	)
	private Study study;

	@OneToOne(fetch = LAZY)
	@JoinColumn(
		name = "user_id",
		insertable = false,
		updatable = false
	)
	private User user;

	public Participant(Study study, User user) {
		this.study = study;
		this.user = user;
	}

}
