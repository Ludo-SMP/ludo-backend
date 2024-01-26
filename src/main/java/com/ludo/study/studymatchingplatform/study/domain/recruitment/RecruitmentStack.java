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
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "recruitment_stack_lnk")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecruitmentStack extends BaseEntity {

	@EmbeddedId
	private RecruitmentStackId id;

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
		name = "stack_id",
		insertable = false,
		updatable = false,
		nullable = false
	)
	private Stack stack;

}
