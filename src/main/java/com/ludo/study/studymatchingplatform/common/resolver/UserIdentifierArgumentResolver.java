package com.ludo.study.studymatchingplatform.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ludo.study.studymatchingplatform.auth.kakao.service.AuthService;
import com.ludo.study.studymatchingplatform.common.interceptor.Authenticated;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.study.service.exception.ServerException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserIdentifierArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String BEARER = "Bearer ";

	private final AuthService authService;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		if (!parameter.hasMethodAnnotation(Authenticated.class)) {
			throw new ServerException("UserIdentifier는 인증된 사용자만 사용 가능합니다. (@Authenticated)");
		}
		return parameter.getParameterType().equals(String.class)
				&& parameter.hasParameterAnnotation(UserIdentifier.class);
	}

	@Override
	public String resolveArgument(final MethodParameter parameterm, final ModelAndViewContainer mavContainer,
			final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
		final String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
		checkHeader(authorizationHeader);
		final String token = authorizationHeader.substring(BEARER.length());
		return authService.findIdentifierByToken(token);
	}

	private void checkHeader(final String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
			throw new AuthenticationException("인증 헤더가 적절하지 않습니다.");
		}
	}

}
