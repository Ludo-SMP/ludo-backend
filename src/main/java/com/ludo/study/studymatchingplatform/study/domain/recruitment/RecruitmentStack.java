package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import java.util.Objects;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentStackId;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

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
@Table(name = "recruitment_stack_lnk")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecruitmentStack extends BaseEntity {

	@EmbeddedId
	@Builder.Default
	private RecruitmentStackId id = new RecruitmentStackId();

	@ManyToOne(fetch = LAZY)
	@MapsId("recruitmentId")
	@JoinColumn(name = "recruitment_id")
	private Recruitment recruitment;

	@ManyToOne(fetch = LAZY)
	@MapsId("stackId")
	@JoinColumn(name = "stack_id")
	private Stack stack;

	public static RecruitmentStack from(
			final Recruitment recruitment,
			final Stack stack
	) {
		final RecruitmentStack recruitmentStack = new RecruitmentStack();
		recruitmentStack.recruitment = recruitment;
		recruitmentStack.stack = stack;

		return recruitmentStack;
	}

	public void registerRecruitmentAndStack(final Recruitment recruitment, final Stack stack) {
		if (this.recruitment == null) {
			this.recruitment = recruitment;
		}
		if (this.stack == null) {
			this.stack = stack;
		}
		this.recruitment.addRecruitmentStack(this);
		this.stack.addRecruitmentStack(this);
	}

	public boolean hasStack(final Stack stack) {
		return Objects.equals(this.stack.getId(), stack.getId());
	}
}
