package com.ludo.study.studymatchingplatform.config;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ludo.study.studymatchingplatform.auth.common.resolver.AuthUserResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthUserResolver authUserResolver;

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authUserResolver);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:3000")
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3000);
	}

}
