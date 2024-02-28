package com.ludo.study.studymatchingplatform.study.domain.recruitment.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApplicantId implements Serializable {

	private Long recruitmentId;

	private Long userId;

}
