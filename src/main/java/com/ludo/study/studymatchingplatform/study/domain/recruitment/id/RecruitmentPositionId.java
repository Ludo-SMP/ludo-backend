package com.ludo.study.studymatchingplatform.study.domain.recruitment.id;

import java.io.Serializable;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class RecruitmentPositionId implements Serializable {

	@Column(name = "recruitment_id")
	private Long recruitmentId;

	@Column(name = "position_id")
	private Long positionId;

	public RecruitmentPositionId(Recruitment recruitment, Position position) {
		this.recruitmentId = recruitment.getId();
		this.positionId = position.getId();
	}
}
