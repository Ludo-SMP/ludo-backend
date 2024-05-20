package com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;

public class StackCategoryFixture {
	public static final StackCategory BACKEND = StackCategory.builder().name("백엔드").build();
	public static final StackCategory FRONTEND = StackCategory.builder().name("프론트엔드").build();

	public static StackCategory createStackCategory(final String name) {
		return StackCategory.builder()
				.name(name)
				.build();
	}

}
