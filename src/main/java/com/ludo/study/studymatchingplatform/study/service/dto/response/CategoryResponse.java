package com.ludo.study.studymatchingplatform.study.service.dto.response;

import com.ludo.study.studymatchingplatform.study.domain.Category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryResponse {

	private final long id;

	private final String name;

	public static CategoryResponse from(final Category category) {
		return new CategoryResponse(category.getId(), category.getName());
	}

}
