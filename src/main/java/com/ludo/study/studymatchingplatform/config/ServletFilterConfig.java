package com.ludo.study.studymatchingplatform.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.common.service.UserDetailsService;
import com.ludo.study.studymatchingplatform.filter.JwtAuthenticationFilter;
import com.ludo.study.studymatchingplatform.user.service.UserService;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ServletFilterConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProvider cookieProvider;
	private final UserDetailsService userDetailsService;
	private final UserService userService;

	@Bean
	public FilterRegistrationBean<Filter> jwtAuthenticationFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

		filterRegistrationBean.setFilter(
				new JwtAuthenticationFilter(jwtTokenProvider, cookieProvider, userDetailsService, userService));
		filterRegistrationBean.setOrder(1);
		addAuthenticationEndpoints(filterRegistrationBean);
		return filterRegistrationBean;
	}

	private void addAuthenticationEndpoints(FilterRegistrationBean<Filter> filterRegistrationBean) {
		filterRegistrationBean.addUrlPatterns("/api/users/*");
		filterRegistrationBean.addUrlPatterns("/api/studies/*");
		filterRegistrationBean.addUrlPatterns("/api/statistics/*");
		filterRegistrationBean.addUrlPatterns("/api/reviews/*");
		filterRegistrationBean.addUrlPatterns("/api/notifications/*");
	}

}
