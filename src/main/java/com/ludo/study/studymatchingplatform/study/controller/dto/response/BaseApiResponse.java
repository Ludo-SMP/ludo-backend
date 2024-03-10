package com.ludo.study.studymatchingplatform.study.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseApiResponse<T> {

	private final boolean ok;
	private final String message;
	private final T data;

	public static <T> BaseApiResponse<T> success(T data) {
		return new BaseApiResponse<>(true, "success", data);
	}

	public static <T> BaseApiResponse<T> fail(String message, T data) {
		return new BaseApiResponse<>(false, message, data);
	}

}
