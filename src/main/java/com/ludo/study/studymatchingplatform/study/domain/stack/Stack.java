package com.ludo.study.studymatchingplatform.study.domain.stack;

import java.util.ArrayList;
import java.util.List;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Stack extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stack_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stack_category_id", nullable = false)
	private StackCategory stackCategory;

	@Column(nullable = false, length = 50)
	@Size(max = 50)
	private String name;

	@Column(length = 2048)
	@Size(max = 2048)
	private String imageUrl;

	@OneToMany(mappedBy = "stack", fetch = FetchType.LAZY)
	@Builder.Default
	private List<RecruitmentStack> recruitmentStacks = new ArrayList<>();

	public void addRecruitmentStack(final RecruitmentStack recruitmentStack) {
		this.recruitmentStacks.add(recruitmentStack);
	}

}

