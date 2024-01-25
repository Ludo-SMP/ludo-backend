package com.ludo.study.studymatchingplatform.study.domain.stack;

import java.util.Collection;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter

public class Stack extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stack_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "stack_category_id")
	private StackCategory stackCategory;

	private String name;

	private String imageUrl;

	@OneToMany(mappedBy = "stack")
	private Collection<RecruitmentStack> recruitmentStack;

	public Collection<RecruitmentStack> getRecruitmentStack() {
		return recruitmentStack;
	}

	public void setRecruitmentStack(
		Collection<RecruitmentStack> recruitmentStack) {
		this.recruitmentStack = recruitmentStack;
	}
}
