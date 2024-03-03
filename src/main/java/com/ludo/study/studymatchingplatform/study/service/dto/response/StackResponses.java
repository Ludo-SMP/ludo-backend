package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;

import lombok.Getter;

@Getter
public class StackResponses {

	private final List<StackCategoryResponse> stackCategory;

	public StackResponses(final List<Stack> stacks, final List<StackCategory> stackCategories) {
		stackCategory = stackCategories
				.stream()
				.map(category -> StackCategoryResponse.from(category, stacks))
				.toList();
	}

}
