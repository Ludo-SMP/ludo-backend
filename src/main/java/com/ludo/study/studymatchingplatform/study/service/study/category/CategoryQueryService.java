package com.ludo.study.studymatchingplatform.study.service.study.category;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {

	private final CategoryRepositoryImpl categoryRepository;

	public Category readCategory(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("id=%d는 존재하지 않는 카테고리입니다.", categoryId)));
	}

}
