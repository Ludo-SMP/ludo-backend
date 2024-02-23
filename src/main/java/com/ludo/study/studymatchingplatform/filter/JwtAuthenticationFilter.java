package com.ludo.study.studymatchingplatform.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

	private boolean notExistCookies(final Cookie[] cookies) {
		return cookies == null;
	}

	private boolean notExistSpecificCookie(final Cookie[] cookies, final String specificCookieName) {
		return Arrays.stream(cookies)
				.map(Cookie::getName)
				.noneMatch(cookieName -> cookieName.equals(specificCookieName));
	}

	private String getAccessToken(final Cookie[] cookies) {
		return Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals("Authorization"))
				.map(Cookie::getValue)
				.findFirst()
				.orElseThrow();
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
									final FilterChain filterChain) throws ServletException, IOException {
		if (!request.getMethod().equals("OPTIONS")) {
			final Optional<String> authToken = cookieProvider.getAuthToken(request);
			if (authToken.isEmpty()) {
				throw new AuthenticationException("Authorization 쿠키가 없습니다.");
			}

			final Claims claims = jwtTokenProvider.verifyAuthTokenOrThrow(authToken.get());
			final AuthUserPayload payload = AuthUserPayload.from(claims);
			request.setAttribute(AUTH_USER_PAYLOAD, payload);
		}

		filterChain.doFilter(request, response);
	}

}
