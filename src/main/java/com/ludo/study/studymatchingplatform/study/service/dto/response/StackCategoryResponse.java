package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class StackCategoryResponse {

	private final long id;

	private final String name;

	@JsonProperty(value = "stacks")
	private final List<StackResponse> stack;

	public static StackCategoryResponse from(final StackCategory stackCategory,
											 final List<Stack> stacks) {
		List<StackResponse> stackResponses = stacks.stream()
				.filter(stack -> stack.getStackCategory().getId().equals(stackCategory.getId()))
				.map(StackResponse::from)
				.toList();
		return new StackCategoryResponse(stackCategory.getId(), stackCategory.getName(), stackResponses);
	}

}
