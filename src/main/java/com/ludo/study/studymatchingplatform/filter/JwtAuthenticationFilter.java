package com.ludo.study.studymatchingplatform.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.common.advice.CommonResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;

	public static final String AUTH_USER_PAYLOAD = "AUTH_USER_PAYLOAD";

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
									final FilterChain filterChain) throws ServletException, IOException {
		if (!request.getMethod().equals("OPTIONS")) {
			final Optional<String> authToken = cookieProvider.getAuthToken(request);
			// 토큰이 null 이면 예외 response 를 반환한다.
			if (authToken.isEmpty()) {
				jwtExceptionHandler(response, HttpStatus.UNAUTHORIZED, "Authorization 쿠키가 없습니다.");
				return;
			}

			try {
				Claims claims = jwtTokenProvider.verifyAuthTokenOrThrow(authToken.get());
				final AuthUserPayload payload = AuthUserPayload.from(claims);
				request.setAttribute(AUTH_USER_PAYLOAD, payload);
			} catch (final AuthenticationException e) {
				// 만료되거나 잘못된 토큰일 경우 예외 response 를 반환한다.
				cookieProvider.clearAuthCookie(response);
				jwtExceptionHandler(response, HttpStatus.UNAUTHORIZED, e.getMessage());
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void jwtExceptionHandler(final HttpServletResponse response, final HttpStatus status,
									 final String message) {
		response.setStatus(status.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			final String json = new ObjectMapper().writeValueAsString(CommonResponse.error(message));
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
