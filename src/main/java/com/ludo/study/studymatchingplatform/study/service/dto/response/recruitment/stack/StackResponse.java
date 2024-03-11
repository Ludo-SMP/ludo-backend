package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.stack;

import com.ludo.study.studymatchingplatform.common.ResourcePath;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StackResponse {

	private final long id;

	private final String name;

	private final String imageUrl;

	public static StackResponse from(final Stack stack) {
		return new StackResponse(stack.getId(), stack.getName(),
				ResourcePath.STACK_IMAGE.getPath() + stack.getImageUrl());
	}
}
