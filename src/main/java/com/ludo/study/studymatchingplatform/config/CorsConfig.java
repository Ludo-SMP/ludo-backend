package com.ludo.study.studymatchingplatform.config;

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

public class CorsConfig implements CorsConfigurationSource {

	private static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:3000");
	private static final List<String> ALLOWED_METHODS = List.of("*");
	private static final List<String> ALLOWED_HEADERS = List.of("*");
	private static final List<String> EXPOSED_HEADERS = List.of("*");
	private static final long MAX_AGE = 3_600L;

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		final CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOrigins(ALLOWED_ORIGINS);
		config.setAllowedMethods(ALLOWED_METHODS);
		config.setAllowedHeaders(ALLOWED_HEADERS);
		config.setExposedHeaders(EXPOSED_HEADERS);
		config.setAllowCredentials(true);
		config.setMaxAge(MAX_AGE);

		return config;
	}
}
