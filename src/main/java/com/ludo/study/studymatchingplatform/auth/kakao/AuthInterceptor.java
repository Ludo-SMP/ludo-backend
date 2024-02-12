package com.ludo.study.studymatchingplatform.auth.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ludo.study.studymatchingplatform.auth.common.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.common.interceptor.Authenticated;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private static final String BEARER = "Bearer ";

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
							 final Object handler) {
		if (!(handler instanceof final HandlerMethod handlerMethod)) {
			return true;
		}
		if (handlerMethod.hasMethodAnnotation(Authenticated.class)) {
			final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			checkHeader(authorizationHeader);
			final String token = authorizationHeader.substring(BEARER.length());
			checkTokenCertify(token);
		}
		return true;
	}

	private void checkHeader(final String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
			throw new AuthenticationException("인증 헤더가 적절하지 않습니다.");
		}
	}

	private void checkTokenCertify(final String token) {
		if (!isCertified(token)) {
			throw new AuthenticationException("토큰이 유효하지 않습니다.");
		}
	}

	public boolean isCertified(final String token) {
		return jwtTokenProvider.isValidToken(token);
	}

}
