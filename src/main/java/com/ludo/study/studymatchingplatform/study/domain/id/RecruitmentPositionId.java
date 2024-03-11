package com.ludo.study.studymatchingplatform.study.domain.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@EqualsAndHashCode
@Getter
public class RecruitmentPositionId implements Serializable {

	private Long recruitmentId;

	private Long positionId;

}
