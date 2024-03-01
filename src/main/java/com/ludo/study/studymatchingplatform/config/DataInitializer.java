package com.ludo.study.studymatchingplatform.config;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepositoryImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {

	private final CategoryRepositoryImpl categoryRepository;
	private final PositionRepositoryImpl positionsRepository;

	@PostConstruct
	public void init() {
		initCategories();
		initPositions();
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

	private void initPositions() {
		savePositions(
				Position.builder()
						.name("백엔드")
						.build(),
				Position.builder()
						.name("프론트엔드")
						.build(),
				Position.builder()
						.name("디자이너")
						.build(),
				Position.builder()
						.name("데브옵스")
						.build()
		);
	}

	private void savePositions(Position... positions) {
		for (Position position : positions) {
			positionsRepository.save(position);
		}
	}

}
