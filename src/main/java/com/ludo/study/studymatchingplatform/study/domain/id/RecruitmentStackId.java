package com.ludo.study.studymatchingplatform.study.domain.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class RecruitmentStackId implements Serializable {

	private Long recruitmentId;

	private Long stackId;

}
