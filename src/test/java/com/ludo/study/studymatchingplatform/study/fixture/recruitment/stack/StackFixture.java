package com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;

public class StackFixture {

	private static Stack createStack(String name, StackCategory stackCategory, int companyCount) {
		return Stack.builder()
				.name(name)
				.stackCategory(stackCategory)
				.companyCount(companyCount)
				.build();
	}

	public static Stack createStack(String name, StackCategory stackCategory) {
		return createStack(name, stackCategory, 0);
	}

}
