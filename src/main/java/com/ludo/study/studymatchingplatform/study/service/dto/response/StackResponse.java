package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.common.ResourcePath;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StackResponse {

	private final long id;

	private final String name;

	private final StackCategoryResponse category;

	private final String imageUrl;

	public static StackResponse from(final Stack stack) {
		final StackCategoryResponse stackCategory = StackCategoryResponse.from(stack.getStackCategory());
		return new StackResponse(stack.getId(), stack.getName(), stackCategory,
				ResourcePath.STACK_IMAGE.getPath() + stack.getImageUrl());
	}
}
