package com.ludo.study.studymatchingplatform.config;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {

	private final CategoryRepositoryImpl categoryRepository;

	@PostConstruct
	public void init() {
		initCategories();
	}

	private void initCategories() {
		saveCategories(
				Category.builder()
						.name("프로젝트")
						.build(),
				Category.builder()
						.name("코딩테스트")
						.build(),
				Category.builder()
						.name("모의면접")
						.build()
		);
	}

	private void saveCategories(Category... categories) {
		for (Category category : categories) {
			categoryRepository.save(category);
		}
	}

}
