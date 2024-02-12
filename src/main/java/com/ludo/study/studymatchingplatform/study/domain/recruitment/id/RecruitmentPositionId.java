package com.ludo.study.studymatchingplatform.study.domain.recruitment.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class RecruitmentPositionId implements Serializable {

	private Long recruitmentId;

	private Long positionId;

}
