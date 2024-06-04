package com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;

public class StackFixture {

	public static final Stack JAVA = Stack.builder()
			.name("java")
			.stackCategory(StackCategoryFixture.BACKEND)
			.companyCount(0)
			.build();
	public static final Stack PYTHON = Stack.builder().
			name("python").
			stackCategory(StackCategoryFixture.BACKEND).
			companyCount(0).
			build();
	public static final Stack JAVA_SCRIPT = Stack.builder()
			.name("javascript")
			.stackCategory(StackCategoryFixture.FRONTEND)
			.companyCount(0)
			.build();
	public static final Stack REACT = Stack.builder()
			.name("react")
			.stackCategory(StackCategoryFixture.FRONTEND)
			.companyCount(0)
			.build();

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
