package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

public class StackFixture {

	public static Stack createStack(String name) {
		return Stack.builder()
				.name(name)
				.build();
	}

}
