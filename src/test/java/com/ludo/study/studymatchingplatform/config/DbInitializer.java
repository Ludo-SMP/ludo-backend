package com.ludo.study.studymatchingplatform.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Profile("test")
@Slf4j
public class DbInitializer {

	private final CategoryRepositoryImpl categoryRepository;
	private final PositionRepositoryImpl positionsRepository;
	private final StackCategoryRepositoryImpl stackCategoryRepository;
	private final StackRepositoryImpl stackRepository;

	@PostConstruct
	public void init() {
		initCategories();
		initPositions();
		initStackCategories();
		initStacks();
	}

	private void initCategories() {
		saveCategories(
				CategoryFixture.CATEGORY_PROJECT,
				CategoryFixture.CATEGORY_CODING_TEST,
				CategoryFixture.CATEGORY_INTERVIEW
		);
	}

	private void saveCategories(Category... categories) {
		for (Category category : categories) {
			categoryRepository.save(category);
		}
	}

	private void initPositions() {
		savePositions(
				PositionFixture.BACKEND,
				PositionFixture.FRONTEND,
				PositionFixture.DESIGNER,
				PositionFixture.DEVOPS
		);
	}

	private void savePositions(Position... positions) {
		for (Position position : positions) {
			positionsRepository.save(position);
		}
	}

	private void initStackCategories() {
		saveStackCategories(
				StackCategoryFixture.BACKEND,
				StackCategoryFixture.FRONTEND
		);
	}

	private void saveStackCategories(StackCategory... stackCategories) {
		for (StackCategory stackCategory : stackCategories) {
			stackCategoryRepository.save(stackCategory);
		}
	}

	private void initStacks() {
		saveStacks(
				StackFixture.JAVA,
				StackFixture.PYTHON,
				StackFixture.REACT,
				StackFixture.JAVA_SCRIPT
		);
	}

	private void saveStacks(Stack... stacks) {
		for (Stack stack : stacks) {
			stackRepository.save(stack);
		}
	}

}
