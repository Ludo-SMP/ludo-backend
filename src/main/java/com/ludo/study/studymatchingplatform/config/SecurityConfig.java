package com.ludo.study.studymatchingplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.cors(cors ->
						cors.configurationSource(new CorsConfig())
				)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req -> req.anyRequest().permitAll()
				)
				.build();
	}

	@Bean // RestTemplate 에 대록 의존성 관리를 스프링 컨테이너가 관리하도록 등록
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
