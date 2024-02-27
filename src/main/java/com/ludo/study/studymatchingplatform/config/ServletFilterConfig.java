package com.ludo.study.studymatchingplatform.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.filter.JwtAuthenticationFilter;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ServletFilterConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;

	@Bean
	public FilterRegistrationBean<Filter> jwtAuthenticationFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

		filterRegistrationBean.setFilter(new JwtAuthenticationFilter(jwtTokenProvider, cookieProvider));
		filterRegistrationBean.setOrder(1);
		addAuthenticationEndpoints(filterRegistrationBean);
		return filterRegistrationBean;
	}

	private void addAuthenticationEndpoints(FilterRegistrationBean<Filter> filterRegistrationBean) {
		filterRegistrationBean.addUrlPatterns("/users/me");
		filterRegistrationBean.addUrlPatterns("/users/me/*");
		filterRegistrationBean.addUrlPatterns("/studies/*");
		filterRegistrationBean.addUrlPatterns("/test/*");
		filterRegistrationBean.addUrlPatterns("/studies/{studyId}/recruitments/{recruitmentId}/cancel");
		filterRegistrationBean.addUrlPatterns("/studies/{studyId}/recruitments/{recruitmentId}/apply");
		filterRegistrationBean.addUrlPatterns("/users/*");
	}

}
