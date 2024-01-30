package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Category;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl {

	private final CategoryJpaRepository categoryJpaRepository;

	public Category save(Category category) {
		categoryJpaRepository.save(category);
		return category;
	}

}
