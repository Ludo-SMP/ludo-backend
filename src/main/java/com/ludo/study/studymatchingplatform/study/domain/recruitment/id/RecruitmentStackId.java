package com.ludo.study.studymatchingplatform.study.domain.recruitment.id;

import java.io.Serializable;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class RecruitmentStackId implements Serializable {

	@Column(name = "recruitment_id")
	private Long recruitmentId;

	@Column(name = "stack_id")
	private Long stackId;

	public RecruitmentStackId(Recruitment recruitment, Stack stack) {
		this.recruitmentId = recruitment.getId();
		this.stackId = stack.getId();
	}
}
