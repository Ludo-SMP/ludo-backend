package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentStackId;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruitment_stack_lnk")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter

public class RecruitmentStack extends BaseEntity {

	@EmbeddedId
	private RecruitmentStackId id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "recruitment_id",
		insertable = false,
		updatable = false
	)
	private Recruitment recruitment;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "stack_id",
		insertable = false,
		updatable = false
	)
	private Stack stack;
}
