package com.ludo.study.studymatchingplatform.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ludo.study.studymatchingplatform.common.interceptor.AuthInterceptor;
import com.ludo.study.studymatchingplatform.common.resolver.UserIdentifierArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthInterceptor authInterceptor;
	private final UserIdentifierArgumentResolver userIdentifierArgumentResolver;

	@Override
	public void addInterceptors(final InterceptorRegistry interceptorRegistry) {
		interceptorRegistry.addInterceptor(authInterceptor);
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(userIdentifierArgumentResolver);
	}

}
