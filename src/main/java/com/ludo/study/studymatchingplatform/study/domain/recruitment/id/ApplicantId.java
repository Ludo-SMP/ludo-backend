package com.ludo.study.studymatchingplatform.study.domain.recruitment.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ApplicantId implements Serializable {

	private Long recruitmentId;

	private Long userId;

}
