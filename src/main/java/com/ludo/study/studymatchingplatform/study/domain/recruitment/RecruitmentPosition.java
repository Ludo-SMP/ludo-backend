package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentPositionId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruitment_position_lnk")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RecruitmentPosition extends BaseEntity {

	@EmbeddedId
	private RecruitmentPositionId id;

	@ManyToOne(fetch = LAZY)
	@MapsId("recruitmentId")
	@JoinColumn(name = "recruitment_id")
	private Recruitment recruitment;

	@ManyToOne(fetch = LAZY)
	@MapsId("positionId")
	@JoinColumn(name = "position_id")
	private Position position;

}
