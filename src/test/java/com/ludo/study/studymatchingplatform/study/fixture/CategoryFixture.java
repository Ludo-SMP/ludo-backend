package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.Category;

public class CategoryFixture {

	public static final String PROJECT = "프로젝트";
	public static final String CODING_TEST = "코딩테스트";
	public static final String INTERVIEW = "인터뷰";

	public static Category createCategory(String name) {
		return Category.builder()
				.name(name)
				.build();
	}

}
