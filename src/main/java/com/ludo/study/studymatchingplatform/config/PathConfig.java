package com.ludo.study.studymatchingplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathConfig implements WebMvcConfigurer {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.addPathPrefix("/api",
				HandlerTypePredicate.forBasePackage("com.ludo.study.studymatchingplatform")
						.and(HandlerTypePredicate.forAnnotation(Controller.class))
		);
	}

}
