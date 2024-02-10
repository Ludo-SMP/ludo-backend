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
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

	@OneToOne
	@JoinColumn(
			name = "stack_category_id",
			nullable = false
	)
	private StackCategory stackCategory;

	@Column(
			nullable = false,
			length = 50
	)
	@Size(max = 50)
	private String name;

	@Column(length = 2048)
	@Size(max = 2048)
	private String imageUrl;

	@OneToMany(mappedBy = "stack")
	private Collection<RecruitmentStack> recruitmentStack;

}
