package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.Category;

public class CategoryFixture {
	public static Category createCategory(String name) {
		return Category.builder()
				.name(name)
				.build();
	}

}
