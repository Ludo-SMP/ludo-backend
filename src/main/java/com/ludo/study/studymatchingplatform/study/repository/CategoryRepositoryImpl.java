package com.ludo.study.studymatchingplatform.study.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Category;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl {

	private final CategoryJpaRepository categoryJpaRepository;

	public Optional<Category> findById(final Long categoryId) {
		return categoryJpaRepository.findById(categoryId);
	}

}
