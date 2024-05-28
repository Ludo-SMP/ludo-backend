package com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;

public class StackCategoryFixture {

	public static StackCategory createStackCategory(final String name) {
		return StackCategory.builder()
				.name(name)
				.build();
	}

}
