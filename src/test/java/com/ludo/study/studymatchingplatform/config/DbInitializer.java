package com.ludo.study.studymatchingplatform.config;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbInitializer {

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
						.name("코딩 테스트")
						.build(),
				Category.builder()
						.name("모의 면접")
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
