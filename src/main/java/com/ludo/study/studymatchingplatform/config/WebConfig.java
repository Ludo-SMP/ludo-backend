package com.ludo.study.studymatchingplatform.config;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ludo.study.studymatchingplatform.auth.common.resolver.AuthUserIdResolver;
import com.ludo.study.studymatchingplatform.auth.common.resolver.AuthUserResolver;
import com.ludo.study.studymatchingplatform.common.ResourcePath;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthUserResolver authUserResolver;
	private final AuthUserIdResolver authUserIdResolver;

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authUserResolver);
		resolvers.add(authUserIdResolver);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(
						"https://ludo.study",
						"https://ludoapi.store",
						"https://local.ludoapi.store:3000",
						"https://local.ludo.study:3000",
						"http://localhost:3000"
				)
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3000);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(ResourcePath.STACK_IMAGE.getPathPattern())
				.addResourceLocations(ResourcePath.STACK_IMAGE.getPathWithClassPath());
	}

}
