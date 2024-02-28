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
import lombok.Builder;
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
	@Builder.Default
	private RecruitmentPositionId id = new RecruitmentPositionId();

	@ManyToOne(fetch = LAZY)
	@MapsId("recruitmentId")
	@JoinColumn(name = "recruitment_id")
	private Recruitment recruitment;

	@ManyToOne(fetch = LAZY)
	@MapsId("positionId")
	@JoinColumn(name = "position_id")
	private Position position;

	public static RecruitmentPosition from(
			final Recruitment recruitment,
			final Position position
	) {
		final RecruitmentPosition recruitmentPosition = new RecruitmentPosition();
		recruitmentPosition.recruitment = recruitment;
		recruitmentPosition.position = position;

		return recruitmentPosition;
	}

	public void registerRecruitment(final Recruitment recruitment) {
		this.recruitment = recruitment;
		this.recruitment.addPosition(this);
	}

}
