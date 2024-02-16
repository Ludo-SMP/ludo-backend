package com.ludo.study.studymatchingplatform.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ludo.study.studymatchingplatform.auth.common.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.filter.JwtAuthenticationFilter;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ServletFilterConfig {

	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public FilterRegistrationBean<Filter> jwtAuthenticationFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

		filterRegistrationBean.setFilter(new JwtAuthenticationFilter(jwtTokenProvider));
		filterRegistrationBean.setOrder(1);
		addAuthenticationEndpoints(filterRegistrationBean);
		return filterRegistrationBean;
	}

	private void addAuthenticationEndpoints(FilterRegistrationBean<Filter> filterRegistrationBean) {
		filterRegistrationBean.addUrlPatterns("/studies");
	}

}
