package com.ludo.study.studymatchingplatform.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.property.ClientRegistrationAndProvider;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.property.ClientRegistrationAndProviderMapper;
import com.ludo.study.studymatchingplatform.auth.naver.service.vo.property.OAuthProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthConfig {

	private final OAuthProperties oAuthProperties;

	public OAuthConfig(OAuthProperties oAuthProperties) {
		this.oAuthProperties = oAuthProperties;
	}

	@Bean
	public InMemoryClientRegistrationAndProviderRepository oAuthProviderRepository() {
		Map<String, ClientRegistrationAndProvider> providers
			= ClientRegistrationAndProviderMapper.mapBy(oAuthProperties);
		return new InMemoryClientRegistrationAndProviderRepository(providers);
	}

}
