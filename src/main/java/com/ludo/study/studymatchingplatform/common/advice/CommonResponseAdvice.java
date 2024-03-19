package com.ludo.study.studymatchingplatform.common.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RestControllerAdvice
@RequiredArgsConstructor
public final class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(final MethodParameter returnType,
							final Class<? extends HttpMessageConverter<?>> converterType) {
		return converterType.isAssignableFrom(MappingJackson2HttpMessageConverter.class);
	}

	@SneakyThrows
	@Override
	public Object beforeBodyWrite(Object body, final MethodParameter returnType,
								  final MediaType selectedContentType,
								  final Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  final ServerHttpRequest request, final ServerHttpResponse response) {
		if (hasError(body) || isStatic(request) || isLocation(response)) {
			return body;
		}
		final DataFieldName annotation = returnType.getMethodAnnotation(DataFieldName.class);
		body = (annotation != null && body instanceof String) ? CommonResponse.success(annotation.value(), body) :
				CommonResponse.success(body);
		return body;
	}

	private boolean isStatic(final ServerHttpRequest request) {
		return request.getURI().getPath().startsWith("/api/static");
	}

	private boolean hasError(final Object body) {
		if (CommonResponse.class.isAssignableFrom(body.getClass())) {
			final CommonResponse resp = (CommonResponse)body;
			return resp.isOk();
		}
		return true;
	}

	private boolean isLocation(final ServerHttpResponse response) {
		return response.getHeaders().containsKey(HttpHeaders.LOCATION);
	}

}
