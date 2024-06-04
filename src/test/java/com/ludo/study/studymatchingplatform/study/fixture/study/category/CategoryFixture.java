package com.ludo.study.studymatchingplatform.study.fixture.study.category;

import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;

public class CategoryFixture {

	public static final String PROJECT = "프로젝트";
	public static final String CODING_TEST = "코딩 테스트";
	public static final String INTERVIEW = "모의 면접";
	public static final Category CATEGORY_PROJECT = Category.builder().name(PROJECT).build();
	public static final Category CATEGORY_CODING_TEST = Category.builder().name(CODING_TEST).build();
	public static final Category CATEGORY_INTERVIEW = Category.builder().name(INTERVIEW).build();

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
