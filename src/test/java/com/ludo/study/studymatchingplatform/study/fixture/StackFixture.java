package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;

public class StackFixture {

	public static Stack createStack(String name, StackCategory stackCategory) {
		return Stack.builder()
				.name(name)
				.stackCategory(stackCategory)
				.build();
	}

}
