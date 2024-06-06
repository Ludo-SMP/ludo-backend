package com.ludo.study.studymatchingplatform.common.advice;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class CommonResponse {

	private final boolean ok;
	private final String message;
	private final Object data;

	public static CommonResponse success(final String nestedFieldName, final Object data) {
		final Map<String, Object> nestedData = Map.of(nestedFieldName, data);
		return new CommonResponse(true, "success", nestedData);
	}

	public static CommonResponse success(final Object data) {
		return new CommonResponse(true, "success", data);
	}

	public static CommonResponse error(final String message) {
		return new CommonResponse(false, message, null);
	}

}
