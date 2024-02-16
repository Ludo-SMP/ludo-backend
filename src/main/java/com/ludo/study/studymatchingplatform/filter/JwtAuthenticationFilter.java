package com.ludo.study.studymatchingplatform.filter;

import java.io.IOException;
import java.util.Arrays;

import com.ludo.study.studymatchingplatform.auth.common.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.study.service.exception.ServerException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		Cookie[] cookies = httpRequest.getCookies();
		if (notExistCookies(cookies)) {
			throw new ServerException("쿠키가 존재하지 않습니다.");
		}
		if (notExistSpecificCookie(cookies, "Authorization")) {
			throw new ServerException("Authorization 쿠키가 없습니다.");
		}
		String accessToken = getAccessToken(cookies);
		jwtTokenProvider.isValidToken(accessToken);
	}

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

}
