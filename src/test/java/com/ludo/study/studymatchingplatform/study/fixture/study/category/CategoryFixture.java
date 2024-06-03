package com.ludo.study.studymatchingplatform.study.fixture.study.category;

import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;

public class CategoryFixture {

	public static final String PROJECT_NAME = "프로젝트";
	public static final String CODING_TEST_NAME = "코딩 테스트";
	public static final String INTERVIEW_NAME = "모의 면접";
	public static final Category CATEGORY_PROJECT = Category.builder().name(PROJECT_NAME).build();
	public static final Category CATEGORY_CODING_TEST = Category.builder().name(CODING_TEST_NAME).build();
	public static final Category CATEGORY_INTERVIEW = Category.builder().name(INTERVIEW_NAME).build();

	public static Category createCategory(String name) {
		return Category.builder()
				.name(name)
				.build();
	}

	public static Category createCategory(Long id, String name) {
		return Category.builder()
				.id(id)
				.name(name)
				.build();
	}

}
