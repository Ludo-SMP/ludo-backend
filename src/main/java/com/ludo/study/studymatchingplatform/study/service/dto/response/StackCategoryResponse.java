package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class StackCategoryResponse {

	private final long id;

	private final String name;

	public static StackCategoryResponse from(final StackCategory stackCategory) {
		return new StackCategoryResponse(stackCategory.getId(), stackCategory.getName());
	}

}
