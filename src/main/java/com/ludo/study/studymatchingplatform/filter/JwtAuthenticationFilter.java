package com.ludo.study.studymatchingplatform.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
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
			if (authToken.isEmpty()) {
				throw new AuthenticationException("Authorization 쿠키가 없습니다.");
			}

			Claims claims = null;
			try {
				claims = jwtTokenProvider.verifyAuthTokenOrThrow(authToken.get());
				final AuthUserPayload payload = AuthUserPayload.from(claims);
				request.setAttribute(AUTH_USER_PAYLOAD, payload);
			} catch (final Exception e) {
				cookieProvider.clearAuthCookie(response);
				throw e;
			}

		}

		filterChain.doFilter(request, response);
	}

}
