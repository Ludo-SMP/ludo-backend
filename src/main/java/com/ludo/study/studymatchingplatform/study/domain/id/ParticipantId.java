package com.ludo.study.studymatchingplatform.study.domain.id;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantId implements Serializable {

	@Column(name = "study_id")
	private Long studyId;

	@Column(name = "user_id")
	private Long userId;
}
