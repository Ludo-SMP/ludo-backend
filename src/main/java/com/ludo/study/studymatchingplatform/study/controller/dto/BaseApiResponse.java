package com.ludo.study.studymatchingplatform.study.controller.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseApiResponse<T> {

	private final boolean ok;
	private final String message;
	private final T data;

	public static <T> BaseApiResponse<T> success(String message, T data) {
		return new BaseApiResponse<>(true, message, data);
	}

	public static <T> BaseApiResponse<T> fail(String message, T data) {
		return new BaseApiResponse<>(false, message, data);
	}

}
