package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentPositionId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "recruitment_position_lnk")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecruitmentPosition extends BaseEntity {

	@EmbeddedId
	private RecruitmentPositionId id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "recruitment_id",
		insertable = false,
		updatable = false,
		nullable = false
	)
	private Recruitment recruitment;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "position_id",
		insertable = false,
		updatable = false,
		nullable = false
	)
	private Position position;

}