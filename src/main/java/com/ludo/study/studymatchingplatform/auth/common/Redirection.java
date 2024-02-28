package com.ludo.study.studymatchingplatform.auth.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.common.properties.ClientProperties;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Redirection {

	private final ClientProperties clientProperties;

	private final Map<String, String> redirectionUrls = new HashMap<>();

	public void to(final String url, final HttpServletResponse response) throws IOException {
		response.sendRedirect(getUrl(url));
	}

	private String getUrl(final String url) {
		if (!redirectionUrls.containsKey(url)) {
			final String clientBaseUrl = clientProperties.getUrl();
			final String fullUrl = clientBaseUrl + url;
			redirectionUrls.put(url, fullUrl);
		}
		return redirectionUrls.get(url);
	}
}
