package com.ludo.study.studymatchingplatform.study.domain.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ParticipantId implements Serializable {

	private Long studyId;

	private Long userId;

	private Long positionId;

}
