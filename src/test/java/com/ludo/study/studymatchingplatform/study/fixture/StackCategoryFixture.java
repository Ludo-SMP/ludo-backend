package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;

public class StackCategoryFixture {

	public static StackCategory createStackCategory(final String name) {
		return StackCategory.builder()
				.name(name)
				.build();
	}

}
