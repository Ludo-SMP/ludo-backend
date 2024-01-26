package com.ludo.study.studymatchingplatform.study.domain;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Column(
		nullable = false,
		columnDefinition = "char(50)"
	)
	private String name;

	@OneToMany(
		mappedBy = "category",
		fetch = LAZY
	)
	private List<Study> studies = new ArrayList<>();

	@Column(nullable = false)
	private String name;

}
