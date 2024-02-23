package com.ludo.study.studymatchingplatform.auth.common.provider;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.common.properties.ClientProperties;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class CookieProvider {

	private final JwtTokenProvider jwtTokenProvider;

	private final ClientProperties clientProperties;
	public static final String AUTH_TOKEN_NAME = "Authorization";

	public Optional<String> getAuthToken(final HttpServletRequest request) {
		final Optional<Cookie> cookie = extractCookie(request, AUTH_TOKEN_NAME);

		if (cookie.isEmpty()) {
			return Optional.empty();
		}

		return Optional.ofNullable(cookie.get().getValue());
	}

	public Optional<Cookie> extractCookie(final HttpServletRequest request, final String cookieName) {
		// 1. cookie 사용을 위해 세팅이 필요 한 경우 -> js go 는 세팅해야함
		// 2. header에 cookies가 없으면 빈 배열이 아니라 null 박아버림 ->이 문제인듯
		final Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return Optional.empty();
		}
		for (final Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				return Optional.ofNullable(cookie);
			}
		}
		return Optional.empty();
	}

	public void setAuthCookie(final String accessToken, final HttpServletResponse response) {
		final int maxAge = Integer.parseInt(jwtTokenProvider.getAccessTokenExpiresIn());
		final Cookie authCookie = createAuthCookie(accessToken, maxAge);
		response.addCookie(authCookie);
	}

	public Cookie createAuthCookie(final String accessToken, final int maxAge) {
		final Cookie cookie = new Cookie(AUTH_TOKEN_NAME, accessToken);

		cookie.setDomain("ludoapi.store");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(clientProperties.isSecure());
		cookie.setAttribute("SameSite", "None");
		cookie.setMaxAge(maxAge);

		return cookie;
	}

	public void clearAuthCookie(final HttpServletResponse response) {
		final Cookie authCookie = createAuthCookie("", 0);
		response.addCookie(authCookie);
	}
}
